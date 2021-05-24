package cz.prague.vida.vocab.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
 * The Class Lesson.
 */
@Entity
@Table(name = "LESSON")
@Data
@EqualsAndHashCode(of = "id")
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


}
