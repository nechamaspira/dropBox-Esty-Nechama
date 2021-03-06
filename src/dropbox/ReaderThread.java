package dropbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderThread extends Thread {
	// read individ lines from stream and do something with it
	private Socket socket;
	private ReaderListener listener;
	
	public ReaderThread(Socket socket, ReaderListener listener) {
		this.socket = socket;
		this.listener = listener;
	}

	public void run() {
		InputStream in;
		try {
			in = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			line = reader.readLine();
			while (line != null && line!= "") {
				listener.onLineRead(line, socket.getOutputStream());
				line = reader.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		// tell listener when program ends
		listener.onCloseSocket(socket);
	}
}
