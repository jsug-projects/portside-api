package jsug.portside.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class AttendRequestForm {
	
	@Size(min=1)
	public List<UUID> ids = new ArrayList<>();
	
	@NotEmpty
	@Email
	public String email;

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	

}
