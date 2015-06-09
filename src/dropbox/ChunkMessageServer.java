package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class ChunkMessageServer extends Messages {
	public static final String ROOT = "./";
	private LinkedList<Socket> sockets;

	public ChunkMessageServer(FileCache fileCache, LinkedList<Socket> sockets) {
		string = "CHUNK";
		this.fileCache = fileCache;
		this.sockets = sockets;

	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);
		// CHUNK [filename] [last modified] [filesize] [offset] [base64 encoded
		// bytes]

		try {

			int offset = Integer.valueOf(array[4]);
			String filename = array[1];
			byte[] b = Base64.decodeBase64(array[5]);

			long lastModified = Long.parseLong(array[2]);

			if (offset == 0) {
				List<File> files = fileCache.getFiles();
				for (int i = 0; i < files.size(); i++) {
					if (files.get(i).getName().equalsIgnoreCase(filename)) {
						fileCache.removeFile(filename);
						break;
					}
				}
			}
			File file = new File(ROOT + "/" + fileCache.getUser() + "/" + array[1]);
			Chunk chunk = new Chunk(file.getAbsolutePath(), b, offset);
			fileCache.addChunk(chunk);

			
			if ((offset + chunk.getChunkSize()) == file.length()) {
				file.setLastModified(Long.parseLong(array[2]));
			}

			
			//send sync message when all the last chunk is uploaded
			int fileSize = Integer.parseInt(array[3]);
			int chunkSize = chunk.getChunkSize();
		
			if ((offset + chunkSize) >= fileSize) {
				for (Socket s : sockets) {
					writer = new PrintWriter(s.getOutputStream());
					writer.println("SYNC " + filename + " " + lastModified + fileSize);
					writer.flush();
					System.out.println("SYNC");
				}
			}

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
