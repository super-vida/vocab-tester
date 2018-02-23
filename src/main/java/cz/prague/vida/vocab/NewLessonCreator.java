package cz.prague.vida.vocab;

import cz.prague.vida.vocab.entity.Lesson;
import cz.prague.vida.vocab.entity.Word;
import cz.prague.vida.vocab.entity.WordGroup;
import cz.prague.vida.vocab.persist.GuiGenerator;
import cz.prague.vida.vocab.persist.PersistentManager;

public class NewLessonCreator {

	public void create(String lessonName, String textArea, Long lessonGroupId) {

		Lesson lesson = new Lesson();
		lesson.setId(GuiGenerator.generateId());
		lesson.setLessonGroupId(lessonGroupId);
		lesson.setName(lessonName);
		lesson.setCorrectCount(0);
		PersistentManager persistentManager = PersistentManager.getInstance();
		persistentManager.persist(lesson);

		String[] lines = textArea.split("\n");
		if (lines == null || lines.length < 1) {
			return;
		}
		lesson.setTotalCount(lines.length);
		for (int ii = 0; ii < lines.length; ii++) {
			System.out.println(lines[ii]);
			String line = lines[ii];
			String[] word = line.split(";");
			// english only one
			Word word1 = persistentManager.findWord(word[0].trim(), "en");
			if (word1 == null) {
				word1 = new Word();
				word1.setLanguage("en");
				word1.setId(GuiGenerator.generateId());
				word1.setText(word[0].trim());
				persistentManager.persist(word1);
			}
			// czech many words
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
				wordGroup.setLessonId(lesson.getId());
				wordGroup.setWord1Id(word1.getId());
				wordGroup.setWord2Id(czechWord.getId());
				persistentManager.persist(wordGroup);
			}
		}
		persistentManager.persist(lesson);
	}
}
