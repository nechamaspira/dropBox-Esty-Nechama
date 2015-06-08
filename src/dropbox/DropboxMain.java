package dropbox;



public class DropboxMain {
	public static void main(String args[]) {
		Thread t = new Thread() {
			public void run() {
				Server server = new Server();
			}
		};
		t.start();
		
		
			Thread thread = new Thread() {
				public void run() {

					Client client = new Client();
					client.startMessages();
				}
			};

			thread.start();
		
		
	}

}
