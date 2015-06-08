package dropbox;

import java.io.OutputStream;
import java.net.Socket;

public interface ReaderListener {

	void onLineRead(String line, OutputStream out);

	void onCloseSocket(Socket socket);

}
