package cz.prague.vida.vocab;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.prague.vida.vocab.entity.Lesson;
import cz.prague.vida.vocab.entity.LessonCheck;
import cz.prague.vida.vocab.entity.Word;
import cz.prague.vida.vocab.entity.WordGroup;
import cz.prague.vida.vocab.persist.GuiGenerator;
import cz.prague.vida.vocab.persist.PersistentManager;

/**
 * The Class VocabHolder.
 */
public class VocabHolderDB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int index = 0;

	private int totalWords = 0;
	private int totalAnswers;
	private int totalCorrectAnswers;
	private String fileName;
	private Lesson lesson;
	private long startCheckTime;

	/** The Constant CHARSET. */
	public static final String CHARSET = "UTF-8";

	private transient List<TestedWord> testedWords;
	private transient Set<TestedWord> incorrectWords;

	/**
	 * Gets the current test word.
	 *
	 * @return the current test word
	 */
	public TestedWord getCurrentTestWord() {
		if (testedWords == null || testedWords.isEmpty()) {
			return new TestedWord(null, null);
		}
		return testedWords.get(index);
	}

	/**
	 * Shuffle words.
	 */
	public void shuffleWords() {
		Collections.shuffle(testedWords);
	}

	/**
	 * Prepare next pair.
	 *
	 * @return true, if successful
	 */
	public boolean prepareNextPair() {
		shuffleWords();
		if (testedWords.isEmpty()) {
			return false;
		}

		if (testedWords.size() > index + 1) {
			++index;
			return true;
		}
		else {
			index = 0;
			return true;
		}

	}

	/**
	 * Load vocabulary file.
	 *
	 * @param lessonName
	 *            the lesson name
	 */
	public void loadVocabularyFile(String lessonName, Lesson less) {
		PersistentManager persistentManager = PersistentManager.getInstance();
		this.totalWords = 0;
		if (lesson == null) {
			this.lesson = persistentManager.findLesson(lessonName);
		}
		else {
			this.lesson = less;
		}
		List<WordGroup> wordGroups = persistentManager.findWordGroups(lesson.getId());
		startCheckTime = System.currentTimeMillis();
		this.fileName = lessonName;
		testedWords = new ArrayList<>();
		Set<TestedWord> set = new HashSet<>();
		incorrectWords = new HashSet<>();
		totalAnswers = 0;
		totalCorrectAnswers = 0;
		try {

			for (WordGroup wordGroup : wordGroups) {
				Word word = persistentManager.getWord(wordGroup.getWord2Id());
				Word translation = persistentManager.getWord(wordGroup.getWord1Id());
				if (addNewWord(set, word, translation)) {
					totalWords++;
				}
			}
		}
		finally {
			testedWords.addAll(set);
		}
	}

	private boolean addNewWord(Set<TestedWord> set, Word word, Word translation) {

		for (TestedWord testedWord : set) {
			if (testedWord.getWord2().getText().equals(translation.getText().trim())) {
				if (testedWord.getWord1().getText().indexOf(word.getText().trim()) < 0) {
					testedWord.getWord1().setText(testedWord.getWord1().getText() + ", " + word.getText());
				}
				return false;
			}
		}
		set.add(new TestedWord(word, translation));
		return true;

	}

	/**
	 * Removes the learned word.
	 *
	 * @param testedWord
	 *            the tested word
	 */
	public void removeLearnedWord(TestedWord testedWord) {
		testedWords.remove(testedWord);
	}

	/**
	 * Adds the answer.
	 *
	 * @param correctAnswer
	 *            the correct answer
	 */
	public void addAnswer(boolean correctAnswer) {
		PersistentManager persistentManager = PersistentManager.getInstance();
		++totalAnswers;
		Word word = getCurrentTestWord().getWord2();
		Date date = new Date();
		if (correctAnswer) {
			++totalCorrectAnswers;
			persistentManager.updateWordCount(word.getId(), date, true);
			word.addCorrectCount();
			word.setCorrectTime(date);
		}
		else {
			persistentManager.updateWordCount(word.getId(), date, false);
			word.addIncorrectCount();
			word.setIncorrectTime(date);
		}
	}

	/**
	 * Gets the total answers.
	 *
	 * @return the total answers
	 */
	public int getTotalAnswers() {
		return totalAnswers;
	}

	/**
	 * Gets the total correct answers.
	 *
	 * @return the total correct answers
	 */
	public int getTotalCorrectAnswers() {
		return totalCorrectAnswers;
	}

	/**
	 * Gets the rest count.
	 *
	 * @return the rest count
	 */
	public int getRestCount() {
		return totalWords - testedWords.size();
	}

	/**
	 * Gets the percentage.
	 *
	 * @return the percentage
	 */
	public String getPercentage() {
		if (totalCorrectAnswers == 0 || totalAnswers == 0) {
			return "0";
		}
		return new DecimalFormat("###").format((totalCorrectAnswers) / (((double) totalAnswers) / 100));
	}

	/**
	 * Adds the incorrect word.
	 *
	 * @param testedWord
	 *            the tested word
	 */
	public void addIncorrectWord(TestedWord testedWord) {
		incorrectWords.add(testedWord);

	}

	/**
	 * Gets the total words.
	 *
	 * @return the total words
	 */
	public int getTotalWords() {
		return totalWords;
	}

	/**
	 * Creates the incorrect word file.
	 */
	public void createIncorrectLesson() {
		if (fileName.endsWith("_CHYBY") || incorrectWords == null || incorrectWords.isEmpty()) {
			return;
		}
		PersistentManager persistentManager = PersistentManager.getInstance();
		Lesson lessonNew = new Lesson();
		lessonNew.setId(GuiGenerator.generateId());
		lessonNew.setName(this.lesson.getName() + "_CHYBY");
		lessonNew.setTotalCount(incorrectWords.size());
		lessonNew.setCorrectCount(0);
		lessonNew.setLessonGroupId(lesson.getLessonGroupId());
		Lesson lessonOld = persistentManager.findLesson(lessonNew.getName());
		if (lessonOld != null) {
			persistentManager.deleteLesson(lessonOld);
		}

		persistentManager.persist(lessonNew);

		if (!incorrectWords.isEmpty()) {
			for (TestedWord word : incorrectWords) {
				Long word1Id = word.getWord2().getId();
				// czech many words
				String[] words = word.getWord1().getText().split(",");
				for (int i = 0; i < words.length; i++) {
					Word w = persistentManager.findWord(words[i].trim(), "cs");
					if (w != null) {
						Long czechWordId = w.getId();
						WordGroup wordGroup = new WordGroup();
						wordGroup.setId(GuiGenerator.generateId());
						wordGroup.setLessonId(lessonNew.getId());
						wordGroup.setWord1Id(word1Id);
						wordGroup.setWord2Id(czechWordId);
						persistentManager.persist(wordGroup);
					}
				}
			}
		}
	}

	/**
	 * Gets the lesson.
	 *
	 * @return the lesson
	 */
	public Lesson getLesson() {
		return lesson;
	}

	/**
	 * Creates the result file.
	 */
	public void updateLessonStats() {
		lesson.setCheckTime(new Date());
		lesson.setTotalCount(getTotalWords());
		lesson.setCorrectCount(getTotalWords() - incorrectWords.size());
		PersistentManager.getInstance().persist(lesson);
		LessonCheck lessonCheck = new LessonCheck();
		lessonCheck.setId(GuiGenerator.generateId());
		lessonCheck.setLessonId(getLesson().getId());
		lessonCheck.setTime(new Date());
		lessonCheck.setTotalCount(getTotalWords());
		lessonCheck.setCorrectCount(getTotalWords() - incorrectWords.size());
		lessonCheck.setDuration(System.currentTimeMillis() - startCheckTime);
		PersistentManager.getInstance().persist(lessonCheck);

	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

}
