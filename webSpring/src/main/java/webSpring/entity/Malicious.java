package webSpring.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

@Entity
public class Malicious {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Type(type = "text")
	private String line;

	public Malicious(Integer id, String line) {
		super();
		this.id = id;
		this.line = line;
	}

	public Malicious() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

}
