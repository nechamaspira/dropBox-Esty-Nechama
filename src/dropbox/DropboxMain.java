package dropbox;

public class DropboxMain {
	public static void main(String args[]){
	Server server = new Server();
	Client client = new Client();
	client.startMessages();
	}
	 
}
