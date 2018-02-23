package cz.prague.vida.vocab;

import java.io.File;
import java.io.FileFilter;

/**
 * The Class WrongFileDeleter.
 */
public class WrongFileDeleter {

	/**
	 * Inits the.
	 *
	 * @return the file[]
	 */
	public File[] init() {
		File dir = new File("vocabulary");
		return dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.getName().toLowerCase().endsWith("_chyby.txt")) {
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Delete.
	 */
	public void delete() {
		File[] directoryListing = init();

		for (int i = 0; i < directoryListing.length; i++) {
			System.out.println("Deleting file '" + directoryListing[i].getName() + "' :" + directoryListing[i].delete());
		}

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		WrongFileDeleter counter = new WrongFileDeleter();

		counter.delete();

	}

}
