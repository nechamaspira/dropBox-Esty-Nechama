package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Client implements ReaderListener{
	private FileCache fileCache;
	private List<Messages> messages;
	private Socket socket;
	
	public Client(){
		try{
			socket = new Socket("localhost", 2009);
			ReaderThread t = new ReaderThread(socket, this);
			t.start();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		fileCache = new FileCache("client");
		messages = new ArrayList<Messages>();
		messages.add(new Sync());
		messages.add(new ChunkMessageClient(fileCache));
		messages.add(new FileMessage(fileCache));
		messages.add(new Files());
		

	}
	



	@Override
	public void onLineRead(String line, OutputStream out) {
		// TODO Auto-generated method stub
		String[] array = line.split(" ");
		Messages message =null;
		for(Messages m: messages){
			if(m.matches(array[0])){
				message = m;
				break;
			}
		}
		
		if(message ==null){
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
}
