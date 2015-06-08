package dropbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Base64;

public class Download extends Messages {

	public Download() {
		string = "DOWNLOAD";
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		writer = new PrintWriter(outStream);
		// CHUNK [filename] [last modified] [filesize] [offset] [base64 encoded
		// bytes]

		// does this find the file that we sent in the name for?
		File file = new File(array[1]);
		int offset = Integer.parseInt(array[2]);
		int chunkLength = Integer.parseInt(array[3]);
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "rw");
			raf.seek(Long.parseLong(array[2]));
			byte[] b = new byte[chunkLength];
			raf.read(b, offset, chunkLength);
			Base64.Encoder encoder = Base64.getEncoder();
			byte[] encoded = encoder.encode(b);

			// send fileSize or chunkSize?
			writer.println("CHUNK " + array[1] + " " + file.lastModified() + " " + file.length() + " " + offset + " "
					+ encoded);
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
