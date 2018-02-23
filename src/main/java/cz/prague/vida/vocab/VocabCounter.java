package cz.prague.vida.vocab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Class VocabCounter.
 */
public class VocabCounter {

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
				if (file.getName().toLowerCase().endsWith(".txt")) {
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Count.
	 *
	 * @return the string
	 */
	public String count() {
		File[] directoryListing = init();
		StringBuilder sb = new StringBuilder("Files: ");
		sb.append(directoryListing.length);
		sb.append("\n");
		Map<String, String> map = new HashMap<>();
		String line = null;
		int duplicates = 0;
		int totalCount = 0;
		for (int i = 0; i < directoryListing.length; i++) {
			try (BufferedReader br = new BufferedReader(new FileReader(directoryListing[i]))) {
				while ((line = br.readLine()) != null) {
					totalCount++;
					String[] word = line.split(";");
					if (!map.containsKey(word[0].trim())) {
						map.put(word[0].trim(), word[0].trim());
					}
					else{
						duplicates++;
					}
//					String ret = map.put(word[0].trim(), word[0].trim());
//					if (ret != null) {
//						duplicates++;
//					}
				}
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}
		sb.append("Total word count: " + totalCount);
		sb.append("\n");
		sb.append("Duplicates words: " + duplicates);
		sb.append("\n");
		sb.append("Distinct words: " + map.size());
		
		try {
			FileWriter fw = new FileWriter("words.txt");
			List<String> list = new ArrayList<>(map.keySet());
			Collections.sort(list);
			for (String object : list) {
				fw.write(object);
				fw.write("\n");
			}
			fw.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List list = new ArrayList<>(map.keySet());
		
		return sb.toString();
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		VocabCounter counter = new VocabCounter();
		System.out.println(counter.count());
	}

}
