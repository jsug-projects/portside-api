package jsug.portside.api;

import org.springframework.beans.factory.annotation.Autowired;

import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.SessionAttendee;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionAttendeeRepository;
import jsug.portside.api.repository.SessionRepository;

public class DummyDataCreator {

	@Autowired
	SessionRepository sessionRepository;
	
	@Autowired
	AttendeeRepository attendeeRepository;
	
	@Autowired
	SessionAttendeeRepository sessionAttendeeRepository;

	
	public void createDummyData() {
		Session fixture = null;
		for (int i=0; i<3; i++) {
			Session session = createSession(i);
			sessionRepository.save(session);
			if (i==0) {
				fixture = session;
			}
		}
		
		Attendee attendee = new Attendee();
		attendee.assignEmail("foo@example.com");
		attendeeRepository.save(attendee);
		
		SessionAttendee sa = new SessionAttendee();
		sa.assign(fixture, attendee);
		sessionAttendeeRepository.save(sa);
		
		
	}
	public static Session createSession(int i) {
		Session session = new Session();
		session.title = "ダミーセッション"+i;
		session.speaker = "ダミースピーカー"+i;
		session.description = "ダミー概要"+i;
		return session;		
	}

}
