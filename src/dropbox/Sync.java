package dropbox;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Sync extends Messages {

	public final int MAXCHUNKSIZE = 512;
	public final String ROOT = "./";

	public Sync(FileCache fileCache) {
		string = "SYNC";
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);

		File file = new File(ROOT + "/server/" + array[1]);
		long fileSize = file.length();
		long sizeLeft = fileSize;
		long offset = 0;

		while (sizeLeft > 0) {

			if (sizeLeft > MAXCHUNKSIZE) {
				writer.println("DOWNLOAD " + file.getName() + " " + offset + " " + MAXCHUNKSIZE);
				writer.flush();
				sizeLeft -= MAXCHUNKSIZE;
				offset += MAXCHUNKSIZE;
			} else {
				writer.println("DOWNLOAD " + file.getName() + " " + offset + " " + sizeLeft);
				writer.flush();
				break;
			}
		}
		System.out.println("DOWNLOAD " + file.getName() + " " + offset + " " + sizeLeft);


	}
}
