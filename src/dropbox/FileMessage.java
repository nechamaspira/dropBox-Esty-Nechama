package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class FileMessage extends Messages {

	public final int MAXCHUNKSIZE = 512;
	private Client client;

	public FileMessage(Client client) {
		this.client = client;
		string = "FILE";
		this.fileCache = client.getFileCache();
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {

		writer = new PrintWriter(outStream);
		List<File> files = fileCache.getFiles();
		Boolean found = false;
		File fileFound = null;
		for (int i = 0; i < files.size(); i++) {
			if (files.get(i).getName().equalsIgnoreCase(array[1])) {
				found = true;
				fileFound = files.get(i);
				break;
			}
		}

		File file = new File(ChunkMessageClient.ROOT + "/" + "server" + "/" + array[1]);

		if (found && fileFound.lastModified() < Long.parseLong(array[2])) {
			fileCache.removeFile(fileFound.getAbsolutePath());
			sendDownloadMessage(file);
		} else if (!found) {
			// download
			sendDownloadMessage(file);
		}

		// upload

		files = fileCache.getFiles();

		client.add(array[1], file.lastModified());
		if (client.isSizeEqual()) {

			ArrayList<String> serverArray = client.getServerString();
			ArrayList<Long> serverArrayDate = client.getServerStringDate();

			for (int i = 0; i < files.size(); i++) {
				found = false;
				File theFile = new File(ChunkMessageClient.ROOT + "/" + fileCache.getUser() + "/"
						+ files.get(i).getName());

				for (int j = 0; j < serverArray.size(); j++) {
					String clientName = files.get(i).getName();
					String serverName = serverArray.get(j);
					if ((clientName.equalsIgnoreCase(serverName))
							&& (files.get(i).lastModified() == serverArrayDate.get(j))) {
						found = true;
						theFile = files.get(i);
						break;
					}
				}
				if (!found) {
					sendUploadMessage(theFile);
				}

			}
		}

	}

	public void sendDownloadMessage(File file) {

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
		System.out.println("DOWNLOAD " + file.getName());

	}

	public void sendUploadMessage(File file) {
		int fileSize = (int) file.length();
		int sizeLeft = fileSize;
		int offset = 0;
		RandomAccessFile raf;
		String encoded = null;

		while (sizeLeft > 0) {
			if (sizeLeft > MAXCHUNKSIZE) {

				try {
					raf = new RandomAccessFile(file, "rw");
					raf.seek(offset);
					byte[] b = new byte[MAXCHUNKSIZE];
					int numRead = raf.read(b, 0, MAXCHUNKSIZE);

					encoded = Base64.encodeBase64String(b);
					writer.println("CHUNK " + file.getName() + " " + file.lastModified() + " " + file.length() + " "
							+ offset + " " + encoded);
					writer.flush();

					sizeLeft -= MAXCHUNKSIZE;
					System.out.println("size left" + sizeLeft);
					offset += MAXCHUNKSIZE;

				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					raf = new RandomAccessFile(file, "rw");
					raf.seek(offset);
					byte[] b = new byte[MAXCHUNKSIZE];
					int numRead = raf.read(b, 0, sizeLeft);
					byte[] b2 = Arrays.copyOf(b, numRead);
					
					encoded = Base64.encodeBase64String(b2);
					writer.println("CHUNK " + file.getName() + " " + file.lastModified() + " " + file.length() + " "
							+ offset + " " + encoded);
					writer.flush();
					// System.out.println("going in chunk message server");
					break;

				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		System.out.println("CHUNK " + file.getName() + " " + file.lastModified() + " " + file.length() + " " + offset
				+ " " + encoded);

	}
}