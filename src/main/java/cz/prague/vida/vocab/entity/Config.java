package cz.prague.vida.vocab.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class Word.
 */
@Entity
@Table(name = "CONFIG")
@Data
@EqualsAndHashCode(of = "id")
public class Config implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "C_ID")
	private Long id;
	@Column(name = "C_TYPE")
	private String type;
	@Column(name = "C_NAME")
	private String name;
	@Column(name = "C_VALUE")
	private String value;

}
