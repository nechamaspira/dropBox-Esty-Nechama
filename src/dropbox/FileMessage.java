package dropbox;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class FileMessage extends Messages {

	
	public static final String ROOT ="./";
	private final int MAXCHUNKSIZE = 512;


	public FileMessage(FileCache fileCache) {
		string = "FILE";
		this.fileCache = fileCache;
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		System.out.println("went in to filemessages");

		writer = new PrintWriter(outStream);
		System.out.println(array[1]);
		List<File> files = fileCache.getFiles();
		Boolean found = false;
		File fileFound = null;
		for (int i = 0; i < files.size(); i++) {
			///change from getAbsolute path
			if (files.get(i).getName().equalsIgnoreCase(array[1])) {
				found = true;
				fileFound = files.get(i);
				break;
			}
		}
	
		//File file = new File(ROOT+"/"+"server"+"/"+array[1]);
		//Long lastMod = fileFound.lastModified();

		
		File file = new File(ROOT+"/"+"server"+"/"+array[1]);
		System.out.println(array[1]);

		if (found && fileFound.lastModified() != Long.parseLong(array[2])) {
			System.out.println("found and date diff");
			fileCache.removeFile(fileFound.getAbsolutePath());
			sendDownloadMessage(file);
		} else if (!found) {
			System.out.println("not found");
			// download
			sendDownloadMessage(file);
		}
		
	

	}

	public void sendDownloadMessage(File file) {
		long fileSize = file.length();
		long sizeLeft = fileSize;
		long offset = 0;

		System.out.println("size left" + sizeLeft);

		while (sizeLeft > 0) {
			if (sizeLeft > MAXCHUNKSIZE) {
				writer.flush();
				//changed
				writer.println("DOWNLOAD " + file.getName() + " " + offset
						+ " " + MAXCHUNKSIZE);
				
				sizeLeft -= MAXCHUNKSIZE;
				offset += MAXCHUNKSIZE;
			} else {
			//	writer.flush();
				//changed
				writer.println("DOWNLOAD " + file.getName() + " " + offset+ " " + sizeLeft);
				
				writer.flush();
				System.out.println("see if going in download");
				break;
			//	sizeLeft=0;
			}
		}
		
	}

}
