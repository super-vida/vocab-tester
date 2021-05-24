package cz.prague.vida.vocab.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * The Class LessonCheck.
 */
@Entity
@Table(name = "LESSON_CHECK")
@Data
@EqualsAndHashCode(of = "id")
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

    public String getDurationFormated() {
        if (duration != null) {
            return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        }
        return "0";
    }

}
