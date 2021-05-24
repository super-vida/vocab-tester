package cz.prague.vida.vocab.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class LessonGroup.
 */
@Entity
@Table(name = "LESSON_GROUP")
@Data
@EqualsAndHashCode(of = "id")
public class LessonGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "LG_ID")
	private Long id;
	@Column(name = "LG_NAME")
	private String name;

}
