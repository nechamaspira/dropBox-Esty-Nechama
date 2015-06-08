package dropbox;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;



public class ListMessage extends Messages {
	private CheckUpload checkUpload;

	public ListMessage(FileCache fileCache){
		string = "LIST";
		this.fileCache = fileCache;
	}


	@Override
	public void perform(OutputStream outStream, String array[]) {
		writer = new PrintWriter(outStream);
		writer.println("FILES " + fileCache.getNumberFiles());
		writer.flush();
		
		System.out.println(fileCache.getNumberFiles()+" files");
		
		List<File> files = fileCache.getFiles();
		
		
		checkUpload = new CheckUpload();
		for(int i =0; i<fileCache.getNumberFiles(); i++){
			//changed from get path
			writer.println("FILE "+ files.get(i).getName() + " " + files.get(i).lastModified() + " "+ files.get(i).length());
			writer.flush();
			System.out.println("see if going in file");

		}	
		
		
	}
}
