package dropbox;

public class DropboxMainClient {

	public static void main(String args[]) {
		Client client = new Client("user1/");
		client.startMessages("CLIENT 1: ");

	}

}
