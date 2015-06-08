package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import org.apache.commons.codec.binary.Base64;


public class Download extends Messages {

	private static final String ROOT = "./";

	public Download(FileCache cache) {
		this.fileCache=cache;
		string = "DOWNLOAD";
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);
		System.out.println("went in download");
		//System.out.println(array.toString());
		// CHUNK [filename] [last modified] [filesize] [offset] [base64 encoded
		// bytes]

		// does this find the file that we sent in the name for?
		File file = new File(ROOT+"/"+fileCache.getUser()+"/"+array[1]);
		int offset = Integer.parseInt(array[2]);
		int chunkLength = Integer.parseInt(array[3]);
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "rw");
			raf.seek(Long.parseLong(array[2]));
			byte[] b = new byte[chunkLength];
			raf.read(b, offset, chunkLength);
			
			String encoded =Base64.encodeBase64String(b);
			
			// send fileSize or chunkSize?
			writer.println("CHUNK " + array[1] + " " + file.lastModified() + " " + file.length() + " " + offset + " "
					+ encoded);
			writer.flush();
			System.out.println("going to chunk");
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
