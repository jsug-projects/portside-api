package jsug.portside.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Size;

public class AssignSpeakersForm {
	@Size(min=1)
	public List<UUID> speakerIds = new ArrayList<>();

}
