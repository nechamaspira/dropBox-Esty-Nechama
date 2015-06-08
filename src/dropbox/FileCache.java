package dropbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class FileCache {
	// this exist on the hardrive

	private static final String ROOT = "C:\\Users\\Esti\\Documents\\COLLEGE\\GitHub\\dropBox-Esty-Nechama\\";
	//private static final String ROOT = "dropbox\\";

	private String user;

	public FileCache(String user) {
		this.user= user;
		new File(ROOT+this.user).mkdir();
	}

	public List<File> getFiles() {
		File folder = new File(ROOT+user);

		File[] listOfFiles = folder.listFiles();

		ArrayList<File> array = new ArrayList<File>(Arrays.asList(listOfFiles));
		return array;

	}
	public void removeFile(String filename){
		File file = new File(filename);
		file.delete();
	}
	
	public int getNumberFiles(){
		return getFiles().size();
	}

	public void addChunk(Chunk chunk) {// array of bites
		File file = new File(chunk.getFilename());
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "rw");
			raf.seek(chunk.getStart());
			byte[] b = Base64.decode(chunk.getInfo());
			//raf.write(chunk.getBytes());
			raf.write(b);
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Base64DecodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Chunk getChunk(File file, String filename, int start, int length) {
		byte[] b = null;
		String encoded = null ;
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.read(b, start, length);
			
			 encoded = Base64.encode(b);

			raf.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return new Chunk(filename, encoded, start);
	}
}
