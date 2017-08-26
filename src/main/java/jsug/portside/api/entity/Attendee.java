package jsug.portside.api.entity;

import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Attendee {

	public UUID id;
	public String email;
	
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}
