package cz.prague.vida.vocab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * The Class VocabPrinter.
 */
public class VocabPrinter {

	private VocabPrinter() {
		super();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		String fileName = args != null && args.length > 0 ? args[0] : null;
		try (BufferedReader br = new BufferedReader(new FileReader(new File("vocabulary", fileName)))) {
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("print.txt"), VocabHolderDB.CHARSET))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] word = line.split(";");
					if (word[0] != null && word[0].trim().length() > 0) {
						bw.write(word[0]);
						bw.newLine();
					}
				}
			}
		}
	}
}
