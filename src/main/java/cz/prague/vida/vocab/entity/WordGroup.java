package cz.prague.vida.vocab.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class WordGroup.
 */
@Entity
@Table(name = "WORD_GROUP")
public class WordGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WG_ID")
	private Long id;
	@Column(name = "WG_L_ID")
	private Long lessonId;
	@Column(name = "WG_W1_ID")
	private Long word1Id;
	@Column(name = "WG_W2_ID")
	private Long word2Id;

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
	 * Gets the lesson id.
	 *
	 * @return the lessonId
	 */
	public Long getLessonId() {
		return lessonId;
	}

	/**
	 * Sets the lesson id.
	 *
	 * @param lessonId
	 *            the lessonId to set
	 */
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}

	/**
	 * Gets the word 1 id.
	 *
	 * @return the word1Id
	 */
	public Long getWord1Id() {
		return word1Id;
	}

	/**
	 * Sets the word 1 id.
	 *
	 * @param word1Id
	 *            the word1Id to set
	 */
	public void setWord1Id(Long word1Id) {
		this.word1Id = word1Id;
	}

	/**
	 * Gets the word 2 id.
	 *
	 * @return the word2Id
	 */
	public Long getWord2Id() {
		return word2Id;
	}

	/**
	 * Sets the word 2 id.
	 *
	 * @param word2Id
	 *            the word2Id to set
	 */
	public void setWord2Id(Long word2Id) {
		this.word2Id = word2Id;
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
		if (!(obj instanceof WordGroup))
			return false;
		WordGroup other = (WordGroup) obj;
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
		builder.append("WordGroup [id=");
		builder.append(id);
		builder.append(", lessonId=");
		builder.append(lessonId);
		builder.append(", word1Id=");
		builder.append(word1Id);
		builder.append(", word2Id=");
		builder.append(word2Id);
		builder.append("]");
		return builder.toString();
	}

}
