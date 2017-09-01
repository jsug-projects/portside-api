package jsug.portside.api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Lists;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.dto.UpdateAttendRequestForm;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;

@RestController
@RequestMapping("/attendees")
public class AttendeeController {

	@Autowired
	AttendeeRepository attendeeRepository;

	@Autowired
	SessionRepository sessionRepository;
	
	@GetMapping
	public List<Attendee> getAllAttendees() {
		return Lists.newArrayList(attendeeRepository.findAll());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public ResponseEntity<?> attend(@Validated @RequestBody AttendRequestForm form, 
			@Value("#{request.requestURL}") String url) {

		Attendee attendee = attendeeRepository.findByEmail(form.email);
		if (attendee != null) {
			updateAttend(attendee, form.ids);
		} else {

			attendee = new Attendee();
			attendee.assignEmail(form.email);
			attendeeRepository.save(attendee);
	
			Iterable<Session> sessions = sessionRepository.findAll(form.ids);
	
			if (form.ids.size() != Lists.newArrayList(sessions).size()) {
				throw new RuntimeException("invalid session id. expected size " + form.ids.size() + " but "
						+ Lists.newArrayList(sessions).size());
			}
	
			for (Session session : sessions) {
				session.attended(attendee);
			}
		}		
		
		URI location = ServletUriComponentsBuilder
				.fromUriString(url)
				.path("/{id}")
				.buildAndExpand(attendee.id)
				.toUri();

		return ResponseEntity.created(location).build();
		
	}

	void updateAttend(Attendee attendee, List<UUID> sessionIds) {
		List<Session> currentAttendSessions = sessionRepository.findByAttendeesId(attendee.id);

		for (Session session : currentAttendSessions) {
			session.unAttended(attendee);
		}

		Iterable<Session> sessions = sessionRepository.findAll(sessionIds);

		if (sessionIds.size() != Lists.newArrayList(sessions).size()) {
			throw new RuntimeException("invalid session id. expected size " + sessionIds.size() + " but "
					+ Lists.newArrayList(sessions).size());
		}

		for (Session session : sessions) {
			session.attended(attendee);
		}
	}

	@GetMapping("/{id}/sessions")
	public List<Session> attendingSessions(@PathVariable UUID id) {
		return sessionRepository.findByAttendeesId(id);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void updateAttend(@Validated @RequestBody UpdateAttendRequestForm form, @PathVariable UUID id) {

		Attendee attendee = attendeeRepository.findOne(id);
		updateAttend(attendee, form.ids);

	}

}
