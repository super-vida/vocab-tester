package cz.prague.vida.vocab;

import java.util.Arrays;
import java.util.List;

import cz.prague.vida.vocab.entity.Lesson;
import cz.prague.vida.vocab.persist.PersistentManager;

/**
 * The Class VocabularyList.
 */
public class VocabularyListDB {
	
	String[] fileList;

	/**
	 * Inits the.
	 */
	public void init() {
		List<Lesson> list = PersistentManager.getInstance().getAllLessons();
		if (list != null) {
			fileList = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				String fileName = list.get(i).getName();
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
		new VocabularyListDB().init();
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
