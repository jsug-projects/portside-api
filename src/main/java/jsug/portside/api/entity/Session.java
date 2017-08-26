package jsug.portside.api.entity;

import java.util.UUID;

import javax.persistence.Entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

//@Entity
public class Session {
	
	public UUID id;
	public String title;
	public String description;
	public String speaker;
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
	
}
