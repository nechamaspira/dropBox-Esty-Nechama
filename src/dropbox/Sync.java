package dropbox;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Sync extends Messages {
	
private final int MAXCHUNKSIZE =512;
	public Sync(){
		string = "SYNC";
	}


	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);
		//SYNC [filename] [last modified] [filesize]
		
		//do i need to cehck if i was the one that uploaded it by checking if i have the file and if the date is the same?
		//download filename offset chunksize
		File file = new File(array[1]);
		long fileSize = file.length();
		long sizeLeft = fileSize;
		long offset = 0;
		while(sizeLeft>0){
			if(sizeLeft >MAXCHUNKSIZE){
			writer.println("DOWNLOAD "+ file.getName()+ " " + offset + " " + MAXCHUNKSIZE);
			sizeLeft -= MAXCHUNKSIZE;
			offset += MAXCHUNKSIZE;
			}
			else{
				writer.println("DOWNLOAD "+ file.getName()+ " " + offset + " " + sizeLeft);
				break;
			}
		}
	}

}
