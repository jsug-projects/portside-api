package jsug.portside.api.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public class SessionAttendeeId implements Serializable {
	public UUID sessionId;
	public UUID attendeeId;
	
}
