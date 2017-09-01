package jsug.portside.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.repository.SessionRepository;

@RestController
public class DeplicatedController {

	@Autowired
	SessionRepository sessionRepository;
	
	
	@GetMapping("/sessionsWithAttendeeCount")
	public List<SessionWithAttendeeCountDto> getAllSessionWithAttendeeCounts() {

		List<SessionWithAttendeeCountDto> list = sessionRepository.findSessionsWithAttendeeCount();

		return list;
	}

}
