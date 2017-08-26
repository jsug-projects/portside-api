package jsug.portside.api.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import jsug.portside.api.entity.Session;

public class SessionWithAttendeeCountDto {

	public Session session;
	public int attendeeCount;

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	

}
