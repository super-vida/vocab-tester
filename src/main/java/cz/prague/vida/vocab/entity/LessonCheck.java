package cz.prague.vida.vocab.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class LessonCheck.
 */
@Entity
@Table(name = "LESSON_CHECK")
public class LessonCheck implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "LC_ID")
	private Long id;
	@Column(name = "LC_L_ID")
	private Long lessonId;
	@Column(name = "LC_TIME")
	private Date time;
	@Column(name = "LC_TOTAL_COUNT")
	private Integer totalCount;
	@Column(name = "LC_CORRECT_COUNT")
	private Integer correctCount;
	@Column(name = "LC_DURATION")
	private Long duration;

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
	 * Gets the time.
	 *
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	public String getTimeFormated() {
		if (time != null) {
			return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(time);
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
	 * Sets the time.
	 *
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
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

	/**
	 * Gets the total count.
	 *
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * Sets the total count.
	 *
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}

	public String getDurationFormated() {
		if (duration != null) {
			return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
		}
		return "0";
	}

	/**
	 * Sets the duration.
	 *
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
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
		if (!(obj instanceof LessonCheck))
			return false;
		LessonCheck other = (LessonCheck) obj;
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
		builder.append("LessonCheck [id=");
		builder.append(id);
		builder.append(", lessonId=");
		builder.append(lessonId);
		builder.append(", time=");
		builder.append(time);
		builder.append(", totalCount=");
		builder.append(totalCount);
		builder.append(", correctCount=");
		builder.append(correctCount);
		builder.append(", duration=");
		builder.append(duration);
		builder.append("]");
		return builder.toString();
	}

}
