package jsug.portside.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.SessionAttendee;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionAttendeeRepository;
import jsug.portside.api.repository.SessionRepository;

@RestController
public class PortsideController {

	
	@Autowired
	SessionRepository sessionRepository;
	
	@Autowired
	AttendeeRepository attendeeRepository;
	
	@Autowired
	SessionAttendeeRepository sessionAttendeeRepository;
	
	@GetMapping("/sessions")
	public List<Session> getAllSessions() {	
		return Lists.newArrayList(sessionRepository.findAll());
	}
	@GetMapping("/sessionsWithAttendeeCount")
	public List<SessionWithAttendeeCountDto> getAllSessionWithAttendeeCounts() {
		
		List<SessionWithAttendeeCountDto> list = sessionRepository.findSessionsWithAttendeeCount();
		
		return list; 
	}
	
		
	@PostMapping("/sessions")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerSession(@Validated @RequestBody Session session) {
		
		sessionRepository.save(session);
		
	}

	@PutMapping("/sessions/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateSession(@Validated @RequestBody Session session, @PathVariable UUID id) {
		
		if (session.id == null) {
			session.updateId(id);
		}		
		sessionRepository.save(session);
	}
	
	@GetMapping("/attendees")
	public List<Attendee> getAllAttendees() {
		return Lists.newArrayList(attendeeRepository.findAll());
	}
	
	
	@PostMapping("/attendees")
	@ResponseStatus(HttpStatus.CREATED)
	public void attend(@Validated @RequestBody AttendRequestForm form) {
		
		Attendee attendee = new Attendee();
		attendee.assignEmail(form.email);
		attendeeRepository.save(attendee);
				
		Iterable<Session> sessions = sessionRepository.findAll(form.ids);
		
		if (form.ids.size() != Lists.newArrayList(sessions).size()) {
			throw new RuntimeException("invalid session id. expected size "+form.ids.size()+" but "+Lists.newArrayList(sessions).size());
		}
		
		for (Session session : sessions) {
			SessionAttendee sessionAttendee = new SessionAttendee();
			sessionAttendee.assign(session, attendee);
			sessionAttendeeRepository.save(sessionAttendee);
		}
	}
	
	
	
//	@PostMapping("/login")
//	public void login(@RequestParam String loginId, @RequestParam String password) {
//		
//		if (loginId.equals("xxx")) {
//			throw new LoginError("invalid id of password.");
//		}
//		
//	}
//	
//	class LoginError extends RuntimeException {
//		public LoginError(String msg) {
//			super(msg);
//		}
//	}
//	
//	@ExceptionHandler(LoginError.class)
//	@ResponseStatus(HttpStatus.UNAUTHORIZED)
//	public void loginError() {
//		
//	}
}
