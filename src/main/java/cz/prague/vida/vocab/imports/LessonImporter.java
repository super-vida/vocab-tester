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
import cz.prague.vida.vocab.persist.GuiGenerator;
import cz.prague.vida.vocab.persist.PersistentManager;

/**
 * The Class LessonImport.
 */
public class LessonImporter {
	

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
	
	
	
	private void importWords(File file, long lessonId){
		PersistentManager persistentManager = PersistentManager.getInstance();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), VocabHolderDB.CHARSET))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] word = line.split(";");
				//english only one
				Word word1 = persistentManager.findWord(word[0].trim(), "en");
				if (word1 == null) {
					word1 = new Word();
					word1.setLanguage("en");
					word1.setId(GuiGenerator.generateId());
					word1.setText(word[0].trim());
					persistentManager.persist(word1);
				}
				//czech many words
				String[] words = word[1].split(",");
				for (int i = 0; i < words.length; i++) {
					Word czechWord = persistentManager.findWord(words[i].trim(), "cs");
					if (czechWord == null) {
						czechWord = new Word();
						czechWord.setLanguage("cs");
						czechWord.setId(GuiGenerator.generateId());
						czechWord.setText(words[i].trim());	
						persistentManager.persist(czechWord);
					}
					WordGroup wordGroup = new WordGroup();
					wordGroup.setId(GuiGenerator.generateId());
					wordGroup.setLessonId(lessonId);
					wordGroup.setWord1Id(word1.getId());
					wordGroup.setWord2Id(czechWord.getId());
					persistentManager.persist(wordGroup);
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
			long id = GuiGenerator.generateId();
			File file = directoryListing[i];
			Lesson lesson = new Lesson();
			lesson.setLessonGroupId(1L);
			lesson.setTotalCount(0);
			lesson.setCorrectCount(0);
			lesson.setId(id);
			lesson.setName(file.getName().replace(".txt", ""));
			PersistentManager.getInstance().persist(lesson);
			importWords(file, id);
		}
	
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		LessonImporter lessonImporter = new LessonImporter();
		try{
		lessonImporter.importLessons();
		}
		finally{
			PersistentManager.getInstance().close();
		}
		
	}

}
