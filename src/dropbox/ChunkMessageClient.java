package dropbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class ChunkMessageClient extends Messages {

	public ChunkMessageClient(FileCache cache) {
		string = "CHUNK";
		this.fileCache=cache;
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		//writer = new PrintWriter(outStream);
		// CHUNK [filename] [last modified] [filesize] [offset] [base64 encoded
		// bytes]

		try {
			File file = new File(array[1]);
			
			//do we need to add this file to our fileCache?
			int offset = Integer.valueOf(array[4]);
			String filename = array[1];
			String encoded = array[5];
			Chunk chunk = new Chunk(filename,encoded,offset);
			fileCache.addChunk(chunk);
			
			
			//if it is the last chunk then change the date last modified
			int fileSize = Integer.parseInt(array[3]);
			int chunkSize = chunk.getChunkSize();
			if((offset+chunkSize) == fileSize){
				file.setLastModified(Long.parseLong(array[2]));
			}
			
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		
	}

}
