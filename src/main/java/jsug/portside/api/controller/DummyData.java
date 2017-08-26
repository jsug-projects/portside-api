package jsug.portside.api.controller;

import java.util.UUID;

import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Session;

public class DummyData {
	public static Session createSession(int i) {
		Session session = new Session();
		session.id = UUID.randomUUID();
		session.title = "ダミーセッション"+i;
		session.speaker = "ダミースピーカー"+i;
		session.description = "ダミー概要"+i;
		return session;		
	}
	
	public static SessionWithAttendeeCountDto createSessionDto(int i) {
		SessionWithAttendeeCountDto dto = new SessionWithAttendeeCountDto();
		dto.session = createSession(i);
		dto.attendeeCount = i;
		return dto;
	}

}
