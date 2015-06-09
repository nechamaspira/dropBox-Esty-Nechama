package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class FileMessage extends Messages {

	
	public static final String ROOT ="./";
	private final int MAXCHUNKSIZE = 512;
	private Client client;

	public FileMessage(Client client) {
		this.client=client;
		string = "FILE";
		this.fileCache = client.getFileCache();
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
		

		if (found && fileFound.lastModified() != Long.parseLong(array[2])) {
			System.out.println("found and date diff");
			fileCache.removeFile(fileFound.getAbsolutePath());
			sendDownloadMessage(file);
		} else if (!found) {
			System.out.println("not found");
			// download
			sendDownloadMessage(file);
		}
		
		
		//upload

		files =fileCache.getFiles();
		
		client.add(array[1], file.lastModified() );
		if(client.isSizeEqual()){
			
				ArrayList<String> serverArray = client.getServerString();
				ArrayList<Long> serverArrayDate = client.getServerStringDate();
				found =false;
				for (int i = 0; i < files.size(); i++) {
					File theFile =new File( ROOT+"/"+fileCache.getUser()+"/"+file.getName());

					for (int j = 0; j < serverArray.size(); j++) {
						if ((files.get(i).getName().equalsIgnoreCase(serverArray.get(j))) && (files.get(i).lastModified() == serverArrayDate.get(j))) {
							found=true;
							theFile = files.get(i);
							break;
						}
					}
					if(!found){
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
				
				writer.println("DOWNLOAD " + file.getName() + " " + offset
						+ " " + MAXCHUNKSIZE);
				writer.flush();
				sizeLeft -= MAXCHUNKSIZE;
				offset += MAXCHUNKSIZE;
			} else {
				writer.println("DOWNLOAD " + file.getName() + " " + offset+ " " + sizeLeft);
				writer.flush();
				System.out.println("see if going in download");
				break;
			}
		}
		
	}
	
	public void sendUploadMessage(File file){
		int fileSize = (int) file.length();
		int sizeLeft = fileSize;
		int offset = 0;
		RandomAccessFile raf;

		while (sizeLeft > 0) {
			if (sizeLeft > MAXCHUNKSIZE) {
				
				try {
					raf = new RandomAccessFile(file, "rw");
					raf.seek(offset);
					byte[] b = new byte[MAXCHUNKSIZE];
					raf.read(b, offset, MAXCHUNKSIZE);
					
					String encoded =Base64.encodeBase64String(b);
					writer.println("CHUNK " + file.getName() + " " + file.lastModified() + " " + file.length() + " " + offset + " "
							+ encoded);
					writer.flush();
					System.out.println("going in chunk message server");
					
					sizeLeft -= MAXCHUNKSIZE;
					offset += MAXCHUNKSIZE;
					
				}
				 catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else {
				try {
					raf = new RandomAccessFile(file, "rw");
					raf.seek(offset);
					byte[] b = new byte[sizeLeft];
					raf.read(b, offset, sizeLeft);
					
					String encoded =Base64.encodeBase64String(b);
					writer.println("CHUNK " + file.getName() + " " + file.lastModified() + " " + file.length() + " " + offset + " "
							+ encoded);
					writer.flush();
					System.out.println("going in chunk message server");
					break;
					
				}
				 catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			}
		}
	

}}