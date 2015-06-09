package dropbox;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

public class ChunkMessageClient extends Messages {

	private static final String ROOT = "./";

	public ChunkMessageClient(FileCache cache) {
		string = "CHUNK";
		this.fileCache=cache;
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		//writer = new PrintWriter(outStream);
		// CHUNK [filename] [last modified] [filesize] [offset] [base64 encoded
		// bytes]
		System.out.println("went in chunk");

		try {
			File file = new File(ROOT+"/"+fileCache.getUser()+"/"+array[1]);
			
			//do we need to add this file to our fileCache?
			int offset = Integer.valueOf(array[4]);
			String filename = ROOT+"/"+fileCache.getUser()+"/"+array[1];
			
			byte[] b =Base64.decodeBase64(array[5]);
			Chunk chunk = new Chunk(filename,b,offset);
			fileCache.addChunk(chunk);
			
			
			//if it is the last chunk then change the date last modified
			int fileSize = Integer.parseInt(array[3]);
			//System.out.println(fileSize);
			//System.out.println(file.exists());
			int chunkSize = chunk.getChunkSize();
		//	System.out.println(chunkSize);
		//	System.out.println(file.lastModified());
			if((offset+chunkSize) == fileSize){
				System.out.println("change date in download " + Long.parseLong(array[2]));
				file.setLastModified(Long.parseLong(array[2]));
			}
			
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
		
		
	}

}
