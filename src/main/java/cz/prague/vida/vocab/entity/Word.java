package cz.prague.vida.vocab.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * The Class Word.
 */
@Entity
@Table(name = "WORD")
@Data
@EqualsAndHashCode(of = "id")
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
