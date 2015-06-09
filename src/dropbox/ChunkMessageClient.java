package dropbox;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

public class ChunkMessageClient extends Messages {

	public static final String ROOT = "./";

	public ChunkMessageClient(FileCache cache) {
		string = "CHUNK";
		this.fileCache = cache;
	}

	@Override
	public void perform(OutputStream outStream, String[] array) {

		try {
			File file = new File(ROOT + "/" + fileCache.getUser() + "/" + array[1]);

			int offset = Integer.valueOf(array[4]);
			String filename = ROOT + "/" + fileCache.getUser() + "/" + array[1];

			byte[] b = Base64.decodeBase64(array[5]);
			Chunk chunk = new Chunk(filename, b, offset);
			fileCache.addChunk(chunk);

			int fileSize = Integer.parseInt(array[3]);
			int chunkSize = chunk.getChunkSize();
			if ((offset + chunkSize) == fileSize) {
				file.setLastModified(Long.parseLong(array[2]));
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

}
