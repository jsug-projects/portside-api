package jsug.portside.api.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Attendee {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(columnDefinition = "binary(16)")	
	public UUID id;
	
	public String email;
	
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


	public void assignEmail(String email) {
		this.email = email;
		
	}	
}
