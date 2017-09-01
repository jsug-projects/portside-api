package jsug.portside.api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
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

import jsug.portside.api.dto.AssignSpeakersForm;
import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.dto.UpdateAttendRequestForm;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.Speaker;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;

@RestController
@RequestMapping("/sessions")
public class SessionController {

	@Autowired
	SessionRepository sessionRepository;

	@Autowired
	SpeakerRepository speakerRepository;
	
	@GetMapping
	public List<Session> getAllSessions() {
		return Lists.newArrayList(sessionRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public Session getSession(@PathVariable UUID id) {
		return sessionRepository.findOne(id);
	}

	@GetMapping("/withAttendeeCount")
	public List<SessionWithAttendeeCountDto> getAllSessionWithAttendeeCounts() {

		List<SessionWithAttendeeCountDto> list = sessionRepository.findSessionsWithAttendeeCount();

		return list;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registerSession(@Validated @RequestBody Session session) {

		sessionRepository.save(session);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(session.id)
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateSession(@Validated @RequestBody Session session, @PathVariable UUID id) {

		if (session.id == null) {
			session.updateId(id);
		}
		sessionRepository.save(session);
	}

	@PutMapping("/{id}/assignSpeakers")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void assignSpeakers(@Validated @RequestBody AssignSpeakersForm form, @PathVariable UUID id) {
		
		List<Speaker> speakers = Lists.newArrayList(speakerRepository.findAll(form.speakerIds));
		
		if (form.speakerIds.size() != speakers.size()) {
			throw new RuntimeException("speakers size not valid. expect "+form.speakerIds.size()+" but "+speakers.size());
		}
		
		Session session = sessionRepository.findOne(id);
		session.assignSpeakers(speakers);		
		
	}
	
}
