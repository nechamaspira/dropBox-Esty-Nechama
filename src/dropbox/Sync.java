package dropbox;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Sync extends Messages {
	
private final int MAXCHUNKSIZE =512;


	public Sync(FileCache fileCache){
		string = "SYNC";
		//fileMessage = new FileMessage(fileCache);
	}


	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);
		//SYNC [filename] [last modified] [filesize]
		
		//download filename offset chunksize
		File file = new File(array[1]);
		long fileSize = file.length();
		long sizeLeft = fileSize;
		long offset = 0;
		while(sizeLeft>0){
			if(sizeLeft >MAXCHUNKSIZE){
				writer.flush();
			writer.println("DOWNLOAD "+ file.getAbsolutePath()+ " " + offset + " " + MAXCHUNKSIZE+"\n");
		//	writer.flush();
			sizeLeft -= MAXCHUNKSIZE;
			offset += MAXCHUNKSIZE;
			}
			else{
				writer.flush();
				writer.println("DOWNLOAD "+ file.getAbsolutePath()+ " " + offset + " " + sizeLeft+"\n");
				//writer.flush();
				break;
			}
		}
	

	}
}
