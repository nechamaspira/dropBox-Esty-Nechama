package dropbox;

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
		messages = new ArrayList<Messages>();
		messages.add(new Sync(fileCache));
		messages.add(new ChunkMessageClient(fileCache));
	//	messages.add(new FileMessage(fileCache));
		messages.add(new FileMessage(this));

		messages.add(new Files());

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

	public void add(String string) {
		serverString.add(string) ;
		
	}
}
