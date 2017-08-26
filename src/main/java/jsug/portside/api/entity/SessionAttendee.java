package jsug.portside.api.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SessionAttendee {

	public Session session;
	public Attendee attendee;

	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	

}
