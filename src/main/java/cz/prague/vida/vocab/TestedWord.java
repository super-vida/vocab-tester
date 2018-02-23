package cz.prague.vida.vocab;

import java.io.Serializable;
import java.text.DecimalFormat;

import cz.prague.vida.vocab.entity.Word;

/**
 * The Class TestedWord.
 */
public class TestedWord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Word word1;
	private Word word2;
	private int correctAnswers = 0;
	private int answers = 0;

	/**
	 * Instantiates a new tested word.
	 *
	 * @param word the word
	 * @param translation the translation
	 */
	public TestedWord(Word word, Word translation) {
		super();
		this.word1 = word;
		this.word2 = translation;
	}

	/**
	 * Gets the word.
	 *
	 * @return the word
	 */
	public Word getWord1() {
		return word1;
	}

	/**
	 * Sets the word.
	 *
	 * @param word the new word
	 */
	public void setWord1(Word word) {
		this.word1 = word;
	}

	/**
	 * Gets the translation.
	 *
	 * @return the translation
	 */
	public Word getWord2() {
		return word2;
	}

	/**
	 * Sets the translation.
	 *
	 * @param translation the new translation
	 */
	public void setWord2(Word translation) {
		this.word2 = translation;
	}

	/**
	 * Gets the correct answers.
	 *
	 * @return the correct answers
	 */
	public int getCorrectAnswers() {
		return correctAnswers;
	}

	/**
	 * Sets the correct answers.
	 *
	 * @param correctAnswers the new correct answers
	 */
	public void setCorrectAnswers(int correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word2 == null) ? 0 : word2.hashCode());
		result = prime * result + ((word1 == null) ? 0 : word1.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TestedWord other = (TestedWord) obj;
		if (word2 == null) {
			if (other.word2 != null) {
				return false;
			}
		}
		else if (!word2.equals(other.word2)) {
			return false;
		}
		if (word1 == null) {
			if (other.word1 != null) {
				return false;
			}
		}
		else if (!word1.equals(other.word1)) {
			return false;
		}
		return true;
	}

	/**
	 * Adds the answer.
	 *
	 * @param correctAnswer the correct answer
	 */
	public void addAnswer(boolean correctAnswer) {
		++answers;
		if (correctAnswer) {
			++correctAnswers;
		}
	}

	/**
	 * Gets the percentage.
	 *
	 * @return the percentage
	 */
	public String getPercentage() {
		if (correctAnswers == 0 || answers == 0) {
			return "0";
		}
		return new DecimalFormat("###").format((correctAnswers) / (((double) answers) / 100));
	}

	/**
	 * Gets the answers.
	 *
	 * @return the answers
	 */
	public int getAnswers() {
		return answers;
	}

}
