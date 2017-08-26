package jsug.portside.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AttendRequestForm {
	
	public List<UUID> ids = new ArrayList<>();
	public String email;

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	

}
