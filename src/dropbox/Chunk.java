package dropbox;

import org.apache.commons.codec.binary.Base64;

public class Chunk {

	private String filename;
<<<<<<< HEAD
	private byte bytes[];
	private int start;
	//private String info;
	
	


	//public Chunk(String filename,String info, int position){
		public Chunk(String filename, byte[] bytes, int position){

		//this.info=info;
		this.filename = filename;
		this.bytes = bytes;
		this.start = position;
	}
	/*public String getInfo() {
=======
	// private byte bytes[];
	private int start;
	private String info;

	public Chunk(String filename, String info, int position) {
		// public Chunk(String filename, byte[] bytes, int start){

		this.info = info;
		this.filename = filename;
		// this.bytes = bytes;
		this.start = position;
	}

	public String getInfo() {
>>>>>>> origin/master
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
<<<<<<< HEAD
	*/
=======

>>>>>>> origin/master
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
<<<<<<< HEAD
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
=======

>>>>>>> origin/master
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
<<<<<<< HEAD
	public int getChunkSize(){
		/*byte[] b = null;
		
			b = Base64.decodeBase64(info);
		
		return b.length;*/
		return bytes.length;
=======

	public int getChunkSize() {
		byte[] b = null;
		b = Base64.decodeBase64(info);
		return b.length;
>>>>>>> origin/master
	}

}
