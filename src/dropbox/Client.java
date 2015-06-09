package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements ReaderListener {
	private FileCache fileCache;
	private List<Messages> messages;
	private Socket socket;
	private PrintWriter writer;
	private ArrayList<String> serverString;
	private ArrayList<Long> serverStringDate;


	public List<Messages> getMessages() {
		return messages;
	}

	public Socket getSocket() {
		return socket;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public int getListCount() {
		return listCount;
	}
	private int listCount;

	public Client(String user) {
		try {
			socket = new Socket("localhost", 2009);
			writer = new PrintWriter(socket.getOutputStream());
		
			ReaderThread t = new ReaderThread(socket, this);
			t.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		fileCache = new FileCache(user);
		serverString = new ArrayList<String>();
		serverStringDate = new ArrayList<Long>();
		messages = new ArrayList<Messages>();
		messages.add(new Sync(fileCache));
		messages.add(new ChunkMessageClient(fileCache));
		messages.add(new FileMessage(this));
		messages.add(new Files(this));
		
		listCount=0;

	}

	public void startMessages() {
		writer.println("LIST");
		writer.flush();
	}

	@Override
	public void onLineRead(String line, OutputStream out) {
		// TODO Auto-generated method stub
		String[] array = line.split(" ");
		Messages message = null;
		for (Messages m : messages) {
			if (m.matches(array[0])) {
				message = m;
				break;
			}
		}

		if (message == null) {
			try {
				throw new InvalidDataException();
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			message.perform(out, array);
		
	}

	@Override
	public void onCloseSocket(Socket socket) {
		// TODO Auto-generated method stub

	}

	public FileCache getFileCache() {
		return fileCache;
	}

	public void setListCount(int count){
		listCount=count;
	}
	public void add(String string, long date) {
		serverString.add(string) ;
		serverStringDate.add(date);
		
	}
	
	public boolean isSizeEqual(){
		return serverString.size() == listCount;
	}
	public ArrayList<String> getServerString() {
		return serverString;
	}

	public ArrayList<Long> getServerStringDate() {
		return serverStringDate;
	}
}
