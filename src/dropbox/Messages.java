package dropbox;

import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class Messages {

	String string;
	FileCache fileCache;
	PrintWriter writer;
	
	public boolean matches(String message){
		return string.equalsIgnoreCase(message);
	}
	public abstract void perform(OutputStream outStream, String[] array);
	
}
