package cz.prague.vida.vocab.entity;

public class EditedWord implements Comparable<EditedWord> {

	private Long wordId;
	private String word;
	private String translation;

	public EditedWord(Long wordGroupId, String word, String translation) {
		super();
		this.wordId = wordGroupId;
		this.word = word;
		this.translation = translation;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	/**
	 * @return the wordId
	 */
	public Long getWordId() {
		return wordId;
	}

	/**
	 * @param wordId
	 *            the wordId to set
	 */
	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((translation == null) ? 0 : translation.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EditedWord other = (EditedWord) obj;
		if (translation == null) {
			if (other.translation != null)
				return false;
		}
		else if (!translation.equals(other.translation))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		}
		else if (!word.equals(other.word))
			return false;
		return true;
	}

	@Override
	public int compareTo(EditedWord o) {
		return this.word.compareTo(o.getWord());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EditedWord [word=");
		builder.append(word);
		builder.append(", translation=");
		builder.append(translation);
		builder.append("]");
		return builder.toString();
	}

}
