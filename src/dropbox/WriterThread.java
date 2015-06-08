package dropbox;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class WriterThread extends Thread {
	private LinkedBlockingQueue<String> queue;
	private List<Messages> messages;
	private OutputStream out;
	//private LinkedList<Socket> sockets;

	public WriterThread(LinkedBlockingQueue<String> queue,List<Messages>messages, OutputStream out) {
		this.queue = queue;
		this.messages=messages;
		this.out =out;
		//this.sockets = sockets;
	}

	@Override
	public void run() {
		String s = null;
		while(true){			
			try {
				s = queue.take();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			String[] array = s.split(" ");
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
			message.perform(out);
		
		} 

	}
	
}
