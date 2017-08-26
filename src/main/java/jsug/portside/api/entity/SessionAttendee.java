package jsug.portside.api.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class SessionAttendee {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(columnDefinition = "binary(16)")	
	public UUID id;
	
	@ManyToOne
	@JoinColumn(name="session_id")
	public Session session;
	
	@ManyToOne
	@JoinColumn(name="attendee_id")
	public Attendee attendee;

	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	

}
