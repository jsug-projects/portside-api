package jsug.portside.api.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Session {
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(columnDefinition = "binary(16)")	
	public UUID id;
	
	public String title;
	public String description;
	public String speaker;

	@OneToMany(mappedBy="session")
	public List<SessionAttendee> sessionAttendees;
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
	
}
