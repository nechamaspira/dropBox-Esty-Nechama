package dropbox;

import java.io.OutputStream;

public class Files extends Messages{
	private Client client;

	public Files(Client client){
		string = "FILES";
		this.client =client;
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {
		client.setListCount(Integer.parseInt(array[1]));
	}

}
