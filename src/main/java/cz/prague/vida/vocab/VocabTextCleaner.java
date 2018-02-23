package cz.prague.vida.vocab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class VocabTextCleaner.
 */
public class VocabTextCleaner {

	/**
	 * Process clean.
	 *
	 * @param fileName
	 *            the file name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void processClean(String fileName) throws IOException {
		File file = new File("vocabulary", fileName);
		File fileNew = new File("vocabulary", fileName + "clean");
		Map<String, String> map = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String row = null;

			while ((row = br.readLine()) != null) {
				String s = row.replace("Odpovìdìli jste správnìrozšíøená slovní zásoba", "");
				s = s.replace("Odpovìdìli jste chybnìzákladní slovní zásoba", "");
				s = s.replace("Odpovìdìli jste chybnìrozšíøená slovní zásoba", "");
				s = s.replace("Odpovìdìli jste správnìzákladní slovní zásoba", "");
				s = s.replace(" - ", ";");
				s = s.replace(" M", "");
				s = s.trim();
				if (!map.containsKey(s)) {
					map.put(s, s);
				}
			}

			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNew), "UTF-8"))) {
				List<String> list = new ArrayList<>(map.keySet());
				Collections.sort(list, new SortIgnoreCase());
				for (String string : list) {
					bw.write(string);
					bw.newLine();
				}
			}
			catch (Exception e) {
				System.out.println(e);
			}

		}
		catch (Exception e) {
			System.out.println(e);
		}
		file.delete();
		fileNew.renameTo(file);

	}

	/**
	 * Process merge.
	 *
	 * @param fileName
	 *            the file name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void processMerge(String fileName) throws IOException {
		File file = new File("vocabulary", fileName);
		File fileNew = new File("vocabulary", fileName + "clean");
		Map<String, String> map = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String row = null;

			while ((row = br.readLine()) != null) {
				if (row != null) {
					String[] s = row.split(";");
					String en = s.length > 0 ? s[0] : "";
					String cs = s.length > 1 ? s[1] : "TODO";
					if (map.containsKey(en)) {
						String csFromMap = map.get(en);

						if (csFromMap.indexOf(cs) < 0) {
							csFromMap = csFromMap + ", " + cs;
							map.put(en, removeDuplicates(csFromMap));
						}
					}
					else {
						map.put(en.trim(), cs.trim());
					}
				}
			}

			List<String> list = new ArrayList<>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				list.add(entry.getKey() + ";" + entry.getValue().replaceAll("\\w,\\w", ", "));
			}

			Collections.sort(list, new SortIgnoreCase());

			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNew), "UTF-8"))) {
				for (String string : list) {
					bw.write(string);
					bw.newLine();
				}
			}
			catch (Exception e) {
				System.out.println(e);
			}

		}
		catch (Exception e) {
			System.out.println(e);
		}

		file.delete();
		fileNew.renameTo(file);
	}

	private String removeDuplicates(String csFromMap) {
		String[] s = csFromMap.split(",");
		Map<String, String> map = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length; i++) {
			map.put(s[i].trim(), s[i].trim());
		}

		for (String string : map.keySet()) {
			if (sb.length() > 0) {
				sb.append(", " + string);
			}
			else {
				sb.append(string);
			}
		}
		return sb.toString();
	}

	/**
	 * Process.
	 *
	 * @param fileName
	 *            the file name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void process(String fileName) throws IOException {
		if (fileName != null) {
			processClean(fileName);
			processMerge(fileName);
		}
		else {
			System.out.println("No file name specified!");
		}
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
		VocabTextCleaner cleaner = new VocabTextCleaner();
		String fileName = args != null && args.length > 0 ? args[0] : null;
		cleaner.process(fileName);

	}

	/**
	 * The Class SortIgnoreCase.
	 */
	public class SortIgnoreCase implements Comparator<Object> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.toLowerCase().compareTo(s2.toLowerCase());
		}
	}

}
