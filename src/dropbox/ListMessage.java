package dropbox;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;



public class ListMessage extends Messages {

	public ListMessage(FileCache fileCache){
		string = "LIST";
		this.fileCache = fileCache;
	}


	@Override
	public void perform(OutputStream outStream, String array[]) {
		writer = new PrintWriter(outStream);
		writer.println("FILES " + fileCache.getNumberFiles());
		List<File> files = fileCache.getFiles();
		for(int i =0; i<fileCache.getNumberFiles(); i++){
			writer.println("FILE "+ files.get(i).getName() + " " + files.get(i).lastModified() + " "+ files.get(i).length()+"\n");
		}

		
	}

}
