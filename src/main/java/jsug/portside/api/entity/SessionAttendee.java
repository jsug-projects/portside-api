package jsug.portside.api.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SessionAttendee {

    @EmbeddedId
    public SessionAttendeeId id = new SessionAttendeeId();
    
	@MapsId("sessionId")
	@ManyToOne
	@JoinColumn(name="session_id")
	@JsonIgnore
	public Session session;
	
	@MapsId("attendeeId")
	@ManyToOne
	@JoinColumn(name="attendee_id")
	public Attendee attendee;

	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


	public void assign(Session session, Attendee attendee) {
		this.session = session;
		this.attendee = attendee;
		
	}	

}
