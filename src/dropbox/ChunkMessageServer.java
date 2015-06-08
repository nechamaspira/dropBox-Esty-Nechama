package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.LinkedList;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class ChunkMessageServer extends Messages {
	
	private LinkedList<Socket> sockets;

	public ChunkMessageServer(FileCache fileCache,LinkedList<Socket> socket) {
		string = "CHUNK";
		this.fileCache=fileCache;
		this.sockets=socket;
		
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);
		// CHUNK [filename] [last modified] [filesize] [offset] [base64 encoded
		// bytes]

	//	Chunk chunk = new Chunk();
	
		try {
			/*File file = new File(array[1]);
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			Long position = Long.parseLong(array[4]);
			raf.seek(position);
			byte[] b = Base64.decode(array[5]);
			raf.write(b);*/
			//do we need to add this file to our fileCache?
			int offset = Integer.valueOf(array[4]);
			Chunk chunk = new Chunk(array[1],array[5],offset);
			fileCache.addChunk(chunk);
			
			//do i need to change the date last modified in the server
			/*if (file.lastModified() != System.currentTimeMillis()) {
				file.setLastModified(System.currentTimeMillis());
			}*/
			
			//needs to send a sync message to all of the clients.
			//check with file size and check with offset
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
