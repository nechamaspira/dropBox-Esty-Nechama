package dropbox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckUpload {
	private List<File> serversFiles;
	private List<File> clientFiles;

	public CheckUpload(List<File> clientFiles) {
		this.serversFiles = new ArrayList<File>();
		this.clientFiles = clientFiles;
	}

	public void addToServerList(File file) {
		serversFiles.add(file);
	}

	public List<File> haveFilesToUpload() {
		List<File> files = new ArrayList<File>();
		boolean found = false;
		for (int i = 0; i < clientFiles.size(); i++) {
			for (int j = 0; j < serversFiles.size(); j++) {
				if (clientFiles.get(i).getName().equalsIgnoreCase(serversFiles.get(j).getName())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				files.add(clientFiles.get(i));
			}
		}
		return files;
	}
}
