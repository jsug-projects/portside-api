package jsug.portside.api.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Staff {

	public String loginId;
	public String password;
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	

	
}
