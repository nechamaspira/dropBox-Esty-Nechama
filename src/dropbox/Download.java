package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

public class Download extends Messages {

	public static final String ROOT = "./";
	public static final int MAXCHUNKSIZE = 512;

	public Download(FileCache cache) {
		this.fileCache = cache;
		string = "DOWNLOAD";
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);

		File file = new File(ROOT + "/" + fileCache.getUser() + "/" + array[1]);
		int offset = Integer.parseInt(array[2]);
		int chunkLength = Integer.parseInt(array[3]);
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "rw");
			raf.seek(Long.parseLong(array[2]));
			byte[] b = new byte[MAXCHUNKSIZE];
			int numRead = raf.read(b, 0, chunkLength);

			byte[] b2 = Arrays.copyOf(b, numRead);
			String encoded = Base64.encodeBase64String(b2);

			writer.println("CHUNK " + array[1] + " " + file.lastModified() + " " + file.length() + " " + offset + " "
					+ encoded);
			writer.flush();
			System.out.println("CHUNK " + array[1] + " " + file.lastModified() + " " + file.length() + " " + offset
					+ " " + encoded);
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
