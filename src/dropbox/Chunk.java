package dropbox;


import org.apache.commons.codec.binary.Base64;


public class Chunk {
	
	private String filename;
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
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}
	*/
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
	public  void setStart(int start) {
		this.start = start;
	}
	public int getChunkSize(){
		/*byte[] b = null;
		
			b = Base64.decodeBase64(info);
		
		return b.length;*/
		return bytes.length;
	}
	
}
