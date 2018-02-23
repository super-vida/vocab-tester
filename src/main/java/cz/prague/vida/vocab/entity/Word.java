package cz.prague.vida.vocab.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * The Class Word.
 */
@Entity
@Table(name = "WORD")
@NamedQueries(value = { @NamedQuery(name = Word.QUERY_UPDATE_CORRECT, query = "update Word w set w.correctCount = w.correctCount + 1, w.correctTime =:date where w.id = :id"), //
		@NamedQuery(name = Word.QUERY_UPDATE_INCORRECT, query = "update Word w set w.incorrectCount = w.incorrectCount + 1, w.incorrectTime =:date where w.id = :id") })
public class Word implements Serializable {
	
	public static final String QUERY_UPDATE_CORRECT = "word.query.updateCorrect";
	public static final String QUERY_UPDATE_INCORRECT = "word.query.updateIncorrect";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "W_ID")
	private Long id;
	@Column(name = "W_LANGUAGE")
	private String language;
	@Column(name = "W_TEXT")
	private String text;
	@Column(name = "W_CORRECT_COUNT")
	private Integer correctCount = 0;
	@Column(name = "W_INCORRECT_COUNT")
	private Integer incorrectCount = 0;
	@Column(name = "W_CORRECT_TIME")
	private Date correctTime;
	@Column(name = "W_INCORRECT_TIME")
	private Date incorrectTime;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language.
	 *
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Gets the text.
	 *
	 * @return the word
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param word
	 *            the word to set
	 */
	public void setText(String word) {
		this.text = word;
	}

	/**
	 * Gets the incorrect count.
	 *
	 * @return the incorrectCount
	 */
	public Integer getIncorrectCount() {
		return incorrectCount;
	}

	/**
	 * Sets the incorrect count.
	 *
	 * @param incorrectCount
	 *            the incorrectCount to set
	 */
	public void setIncorrectCount(Integer incorrectCount) {
		this.incorrectCount = incorrectCount;
	}

	/**
	 * Gets the correct count.
	 *
	 * @return the correctCount
	 */
	public Integer getCorrectCount() {
		return correctCount;
	}

	/**
	 * Sets the correct count.
	 *
	 * @param correctCount
	 *            the correctCount to set
	 */
	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
	}

	public Date getCorrectTime() {
		return correctTime;
	}

	public void setCorrectTime(Date correctTime) {
		this.correctTime = correctTime;
	}

	public Date getIncorrectTime() {
		return incorrectTime;
	}

	public void setIncorrectTime(Date incorrectTime) {
		this.incorrectTime = incorrectTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Word))
			return false;
		Word other = (Word) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Word [id=");
		builder.append(id);
		builder.append(", word=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}

	public void addCorrectCount() {
		if (correctCount == null) {
			correctCount = 1;
		}
		else {
			correctCount++;
		}
	}
	public void addIncorrectCount() {
		if (incorrectCount == null) {
			incorrectCount = 1;
		}
		else {
			incorrectCount++;
		}
	}

}
