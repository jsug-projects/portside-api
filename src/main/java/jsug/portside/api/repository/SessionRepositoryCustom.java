package jsug.portside.api.repository;

import java.util.List;

import jsug.portside.api.dto.SessionWithAttendeeCountDto;

public interface SessionRepositoryCustom {

	List<SessionWithAttendeeCountDto> findSessionsWithAttendeeCount();
	
}
