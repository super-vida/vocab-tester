package cz.prague.vida.vocab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Class VocabHolder.
 */
public class VocabHolder implements Serializable {

//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	private int index = 0;
//
//	private int totalWords = 0;
//	private int totalAnswers;
//	private int totalCorrectAnswers;
//	private String fileName;
//
//	/** The Constant CHARSET. */
//	public static final String CHARSET = "UTF-8";
//
//	private transient List<TestedWord> testedWords;
//	private transient Set<TestedWord> incorrectWords;
//
//	/**
//	 * Gets the current test word.
//	 *
//	 * @return the current test word
//	 */
//	public TestedWord getCurrentTestWord() {
//		if (testedWords == null || testedWords.isEmpty()) {
//			return new TestedWord("", "");
//		}
//		return testedWords.get(index);
//	}
//
//	/**
//	 * Shuffle words.
//	 */
//	public void shuffleWords() {
//		Collections.shuffle(testedWords);
//	}
//
//	/**
//	 * Prepare next pair.
//	 *
//	 * @return true, if successful
//	 */
//	public boolean prepareNextPair() {
//		shuffleWords();
//		if (testedWords.isEmpty()) {
//			return false;
//		}
//
//		if (testedWords.size() > index + 1) {
//			++index;
//			return true;
//		}
//		else {
//			index = 0;
//			return true;
//		}
//
//	}
//
//	/**
//	 * Load vocabulary file.
//	 *
//	 * @param fileName
//	 *            the file name
//	 */
//	public void loadVocabularyFile(Object fileName) {
//		this.totalWords = 0;
//		this.fileName = (String) fileName;
//		File file = new File("vocabulary", this.fileName + ".txt");
//		testedWords = new ArrayList<>();
//		Set<TestedWord> set = new HashSet<>();
//		incorrectWords = new HashSet<>();
//		totalAnswers = 0;
//		totalCorrectAnswers = 0;
//		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARSET))){
//			String line = null;
//			while ((line = br.readLine()) != null) {
//				if (line.trim().length() > 0) {
//					String[] s = line.split(";");
//					if (s.length > 1) {
//						String word = s[1];
//						String translation = s[0];
//						index = 0;
//						if (addNewWord(set, word.trim(), translation.trim())) {
//							totalWords++;
//						}
//					}
//				}
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		finally {
//			testedWords.addAll(set);
//		}
//
//	}
//
//	private boolean addNewWord(Set<TestedWord> set, String word, String translation) {
//
//		for (TestedWord testedWord : set) {
//			if (testedWord.getTranslation().equals(translation.trim())) {
//				if (testedWord.getWord().indexOf(word.trim()) < 0) {
//					testedWord.setWord(testedWord.getWord() + ", " + word);
//				}
//				return false;
//			}
//		}
//		set.add(new TestedWord(word.trim(), translation.trim()));
//		return true;
//
//	}
//
//	/**
//	 * Removes the learned word.
//	 *
//	 * @param testedWord
//	 *            the tested word
//	 */
//	public void removeLearnedWord(TestedWord testedWord) {
//		testedWords.remove(testedWord);
//	}
//
//	/**
//	 * Adds the answer.
//	 *
//	 * @param correctAnswer
//	 *            the correct answer
//	 */
//	public void addAnswer(boolean correctAnswer) {
//		++totalAnswers;
//		if (correctAnswer) {
//			++totalCorrectAnswers;
//		}
//	}
//
//	/**
//	 * Gets the total answers.
//	 *
//	 * @return the total answers
//	 */
//	public int getTotalAnswers() {
//		return totalAnswers;
//	}
//
//	/**
//	 * Gets the total correct answers.
//	 *
//	 * @return the total correct answers
//	 */
//	public int getTotalCorrectAnswers() {
//		return totalCorrectAnswers;
//	}
//
//	/**
//	 * Gets the rest count.
//	 *
//	 * @return the rest count
//	 */
//	public int getRestCount() {
//		return totalWords - testedWords.size();
//	}
//
//	/**
//	 * Gets the percentage.
//	 *
//	 * @return the percentage
//	 */
//	public String getPercentage() {
//		if (totalCorrectAnswers == 0 || totalAnswers == 0) {
//			return "0";
//		}
//		return new DecimalFormat("###").format((totalCorrectAnswers) / (((double) totalAnswers) / 100));
//	}
//
//	/**
//	 * Adds the incorrect word.
//	 *
//	 * @param testedWord
//	 *            the tested word
//	 */
//	public void addIncorrectWord(TestedWord testedWord) {
//		incorrectWords.add(testedWord);
//
//	}
//
//	/**
//	 * Gets the total words.
//	 *
//	 * @return the total words
//	 */
//	public int getTotalWords() {
//		return totalWords;
//	}
//
//	/**
//	 * Creates the incorrect word file.
//	 */
//	public void createIncorrectWordFile() {
//		if (fileName.endsWith("_CHYBY") || incorrectWords == null || incorrectWords.isEmpty()) {
//			return;
//		}
//		File file = new File("vocabulary", this.fileName + "_CHYBY.txt");
//		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), CHARSET))){
//			if (!incorrectWords.isEmpty()) {
//				for (TestedWord word : incorrectWords) {
//					bw.write(word.getTranslation());
//					bw.write(";");
//					bw.write(word.getWord());
//					bw.write("\n");
//				}
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void createResultFile() {
//		
//		File file = new File("results.txt");
//		if (!file.exists()) {
//			try {
//				file.createNewFile();
//			}
//			catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		
//		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true), CHARSET))){
//			bw.write(new SimpleDateFormat("dd.MM.yyyy HH:mm.ss").format(new Date()));
//			bw.write("\t" + fileName);
//			bw.write("\terrors:" + (incorrectWords == null ? "0" : incorrectWords.size()));
//			bw.write("\n");
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public String getFileName() {
//		return fileName;
//	}

}
