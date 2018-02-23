package cz.prague.vida.vocab;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 * The Class VocabularyList.
 */
public class VocabularyList {

	String[] fileList;

	/**
	 * Inits the.
	 */
	public void init() {
		File dir = new File("vocabulary");
		File[] directoryListing = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.getName().toLowerCase().endsWith(".txt")) {
					return true;
				}
				return false;
			}
		});
		if (directoryListing != null) {
			fileList = new String[directoryListing.length];
			for (int i = 0; i < directoryListing.length; i++) {
				String fileName = directoryListing[i].getName();
				fileList[i] = fileName.endsWith(".txt") ? fileName.substring(0, fileName.length() - 4) : fileName;
			}
		}
		Arrays.sort(fileList);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		new VocabularyList().init();
	}

	/**
	 * Gets the file list.
	 *
	 * @return the file list
	 */
	public String[] getFileList() {
		return fileList;
	}

}
