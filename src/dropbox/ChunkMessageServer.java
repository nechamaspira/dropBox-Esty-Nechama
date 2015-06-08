package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class ChunkMessageServer extends Messages {
	
	private LinkedList<Socket> sockets;

	public ChunkMessageServer(FileCache fileCache,LinkedList<Socket> sockets) {
		string = "CHUNK";
		this.fileCache=fileCache;
		this.sockets=sockets;
		
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);
		// CHUNK [filename] [last modified] [filesize] [offset] [base64 encoded
		// bytes]

	
		try {
	
			int offset = Integer.valueOf(array[4]);
			String filename = array[1];
			String encoded = array[5];
			long  lastModified = Long.parseLong(array[2]);
			
			if(offset==0){
				List<File> files = fileCache.getFiles();
				for(int i=0; i<filename.length(); i++){
					if(files.get(i).getAbsolutePath().equalsIgnoreCase(filename)){
						fileCache.removeFile(filename);
						break;
					}
				}
			}
			Chunk chunk = new Chunk(filename,encoded,offset);
			fileCache.addChunk(chunk);
			
			
			int fileSize = Integer.parseInt(array[3]);
			int chunkSize = chunk.getChunkSize();
			if((offset+chunkSize) == fileSize){
				for(Socket s: sockets){
					writer = new PrintWriter(s.getOutputStream());
					writer.flush();
					writer.println("SYNC "+ filename + " "+lastModified + fileSize+"\n");
				}
			}
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
