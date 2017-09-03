package jsug.portside.api;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.Speaker;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;

public class DummyDataCreator {

	@Autowired
	SessionRepository sessionRepository;
	
	@Autowired
	AttendeeRepository attendeeRepository;
	
	@Autowired
	SpeakerRepository speakerRepository;

	@Transactional
	public void createDummyData() throws Exception {
		Speaker speaker1 = createSpeaker(0);
		speaker1.updateImage(FileCopyUtils.copyToByteArray(new ClassPathResource("/flaky.png").getInputStream()));
		speakerRepository.save(speaker1);
		Speaker speaker2 = createSpeaker(1);		
		speaker2.updateImage(FileCopyUtils.copyToByteArray(new ClassPathResource("/slack.png").getInputStream()));
		speakerRepository.save(speaker2);
				
		Session sessionFixture = null;
		for (int i=0; i<3; i++) {
			Session session = createSession(i);
			sessionRepository.save(session);
			if (i==0) {
				sessionFixture = session;
			}
		}
		sessionFixture.assignSpeakers(speaker1, speaker2);
		
		
		Attendee attendee = new Attendee();
		attendee.assignEmail("foo@example.com");
		attendeeRepository.save(attendee);
		
		sessionFixture.attendees.add(attendee);
		
	}
	private Speaker createSpeaker(int i) {
		Speaker speaker = new Speaker();
		speaker.name = "名前"+i;
		speaker.belongTo ="所属"+i;
		speaker.profile = "プロフィール"+i;
		return speaker;
	}

	private Session createSession(int i) {
		Session session = new Session();
		session.title = "ダミーセッション"+i;
		session.speaker = "ダミースピーカー"+i;
		session.description = "ダミー概要"+i;
		return session;		
	}

}
