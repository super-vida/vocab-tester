package cz.prague.vida.vocab.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * The Class Lesson.
 */
@Entity
@Table(name = "LESSON")
@NamedQueries(value = { @NamedQuery(name = Lesson.QUERY_UPDATE_OLD_LESSONS, query = "update Lesson ls set ls.correctCount = 0 where ls.correctCount > 0 and ls.checkTime < :time")})
public class Lesson implements Serializable {
	
	public static final String QUERY_UPDATE_OLD_LESSONS = "lesson.query.updateOldLessons";

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "L_ID")
	private Long id;
	@Column(name = "L_LG_ID")
	private Long lessonGroupId;
	@Column(name = "L_NAME")
	private String name;
	@Column(name = "L_CHECK_TIME")
	private Date checkTime;
	@Column(name = "L_TOTAL_COUNT")
	private Integer totalCount;
	@Column(name = "L_CORRECT_COUNT")
	private Integer correctCount = 0;

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
	 * @return the lessonGroupId
	 */
	public Long getLessonGroupId() {
		return lessonGroupId;
	}

	/**
	 * @param lessonGroupId
	 *            the lessonGroupId to set
	 */
	public void setLessonGroupId(Long lessonGroupId) {
		this.lessonGroupId = lessonGroupId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the checkTime
	 */
	public Date getCheckTime() {
		return checkTime;
	}

	public String getCheckTimeFormated() {
		if (checkTime != null) {
			return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(checkTime);
		}
		return "";
	}

	public String getPercentage() {
		if (correctCount == null || totalCount == null || correctCount == 0 || totalCount == 0) {
			return "0";
		}
		return new DecimalFormat("###").format((correctCount) / (((double) totalCount) / 100)) + "%";
	}

	/**
	 * @param checkTime
	 *            the checkTime to set
	 */
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	/**
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the correctCount
	 */
	public Integer getCorrectCount() {
		return correctCount;
	}

	/**
	 * @param correctCount
	 *            the correctCount to set
	 */
	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
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
		if (!(obj instanceof Lesson))
			return false;
		Lesson other = (Lesson) obj;
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
		builder.append("Lesson [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
