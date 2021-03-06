package dropbox;

public class Chunk {

	private String filename;

	private byte bytes[];
	private int start;

	public Chunk(String filename, byte[] bytes, int position) {

		this.filename = filename;
		this.bytes = bytes;
		this.start = position;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getChunkSize() {
		return bytes.length;

	}
}
