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
		writer = new PrintWriter(outStream);
		// CHUNK [filename] [last modified] [filesize] [offset] [base64 encoded
		// bytes]

		// do i need to create a new file every time the chunk message is
		// received? will it go to the same file as before?
		try {
			File file = new File(array[1]);
			///RandomAccessFile raf = new RandomAccessFile(file, "rw");
			//Long position = Long.parseLong(array[4]);
			//raf.seek(position);
			//byte[] b = Base64.decode(array[5]);
			//raf.write(b);
			//do we need to add this file to our fileCache?
			int offset = Integer.valueOf(array[4]);
			Chunk chunk = new Chunk(array[1],array[5],offset);
			fileCache.addChunk(chunk);
			
			
			//do I need to do this every time? he said to change the date after you download but now the date in teh file will be different than
			//the date in teh server so next time it will think it needs to be uploaded again
			if(file.lastModified()!= System.currentTimeMillis()){
				file.setLastModified(System.currentTimeMillis());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		
	}

}
