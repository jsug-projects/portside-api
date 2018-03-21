package jsug.portside.api.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
		return sessionRepository.findById(id).orElse(null);
	}

	@GetMapping("/withAttendeeCount")
	public List<SessionWithAttendeeCountDto> getAllSessionWithAttendeeCounts() {

		List<SessionWithAttendeeCountDto> list = sessionRepository.findSessionsWithAttendeeCount();

		return list;
	}


	
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public ResponseEntity<?> registerSession(@Validated @RequestBody Session session) {

		for (Speaker speaker : session.speakers) {
			speakerRepository.save(speaker);
		}
		
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
	@Transactional
	public void updateSession(@Validated @RequestBody Session newSession, @PathVariable UUID id) {

		Session managedSession = sessionRepository.findById(id).orElse(null);
		
		managedSession.updateState(newSession);
		
		//delete old speaker
		for (Speaker speaker : managedSession.speakers) {
			for (Session speakerSession : speaker.sessions) {
				if (speakerSession != managedSession) {
					speakerSession.unAssingnSeaker(speaker);
				}
			}
			speakerRepository.delete(speaker);
		}

		//insert new speaker
		for (Speaker speaker : newSession.speakers) {
			speakerRepository.save(speaker);
		}
		managedSession.assignSpeakers(newSession.speakers);		

	}

	@PutMapping("/{id}/assignSpeakers")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void assignSpeakers(@Validated @RequestBody AssignSpeakersForm form, @PathVariable UUID id) {
		
		List<Speaker> speakers = Lists.newArrayList(speakerRepository.findAll(form.speakerIds));
		
		if (form.speakerIds.size() != speakers.size()) {
			throw new RuntimeException("speakers size not valid. expect "+form.speakerIds.size()+" but "+speakers.size());
		}
		
		Session session = sessionRepository.findById(id).orElse(null);
		session.assignSpeakers(speakers);		
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteSession(@PathVariable UUID id) {
		
		Session session = sessionRepository.findById(id).orElse(null);
		for (Speaker speaker : session.speakers) {
			for (Session speakerSession : speaker.sessions) {
				if (speakerSession != session) {
					speakerSession.unAssingnSeaker(speaker);
				}
			}
			speakerRepository.delete(speaker);
		}
		sessionRepository.delete(session);
		
	}
	
	
}
