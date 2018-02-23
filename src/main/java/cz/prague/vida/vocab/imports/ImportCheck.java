package cz.prague.vida.vocab.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import cz.prague.vida.vocab.VocabHolderDB;
import cz.prague.vida.vocab.entity.Lesson;
import cz.prague.vida.vocab.entity.Word;
import cz.prague.vida.vocab.entity.WordGroup;
import cz.prague.vida.vocab.persist.PersistentManager;

/**
 * The Class LessonImport.
 */
public class ImportCheck {
	
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
	
	
	
	private void check(File file){
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), VocabHolderDB.CHARSET))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] word = line.split(";");
				//english only one
				Word word1 = PersistentManager.getInstance().findWord(word[0].trim(), "en");
				if (word1 == null) {
					System.out.println("Can not find word " + word[0].trim());
				}
				
				
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Count.
	 *
	 * @return the string
	 */
	public void importLessons() {
		File[] directoryListing = init();
		for (int i = 0; i < directoryListing.length; i++) {
			File file = directoryListing[i];
			check(file);
		}
	
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ImportCheck lessonImporter = new ImportCheck();
		try{
		lessonImporter.importLessons();
		}
		finally{
			PersistentManager.getInstance().close();
		}
		
	}

}
