package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Server implements ReaderListener {
	private List<Messages> messages;
	private FileCache fileCache;
	private ServerSocket serverSocket;
	private LinkedList<Socket> sockets;

	public Server() {
		this.fileCache = new FileCache("server");
		sockets = new LinkedList<Socket>();
		this.messages = new ArrayList<Messages>();
		messages.add(new ListMessage(this.fileCache));
		messages.add(new ChunkMessageServer(fileCache,sockets));
		messages.add(new Download());
		

		try {

			this.serverSocket = new ServerSocket(2009);
			Socket socket;
			while (true) {
				socket = serverSocket.accept();
				synchronized (sockets) {
					sockets.add(socket);
				}
				ReaderThread readerThread = new ReaderThread(socket, this);
				readerThread.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onLineRead(String line, OutputStream out) {
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
		try {
			socket.close();
			sockets.remove(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	


}
