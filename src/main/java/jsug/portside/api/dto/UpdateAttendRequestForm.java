package jsug.portside.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateAttendRequestForm {
	@Size(min=1)
	public List<UUID> ids = new ArrayList<>();
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	

}
