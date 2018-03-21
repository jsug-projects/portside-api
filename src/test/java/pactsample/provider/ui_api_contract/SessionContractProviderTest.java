package pactsample.provider.ui_api_contract;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import jsug.portside.api.PortsideApplication;
import jsug.portside.api.controller.TestUtils;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.Speaker;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;

@RunWith(SpringRestPactRunner.class)
@Provider("portside-api")
@PactFolder("./target/pacts")
@SpringBootTest(classes=PortsideApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SessionContractProviderTest {

    //@Beforeのタイミングでインスタンス作ると上手く動かず、ランダムポートが渡せないので、仕方なく固定のポートにする
	@TestTarget
	public HttpTarget target = new HttpTarget("http", "localhost", 8088);
		
	@Autowired
	DataSource ds;

	@Autowired
	PlatformTransactionManager tm;

	@Autowired
	SessionRepository sessionRepository;
	
	@Autowired
	SpeakerRepository speakerRepository;
	
	@Autowired
	AttendeeRepository attendeeRepository;
	
	private Session createSession(int i) {
		Session session = new Session();
		session.title = "ダミーセッション"+i;
		session.speaker = "ダミースピーカー"+i;
		session.description = "ダミー概要"+i;
		return session;		
	}
	
	private Speaker createSpeaker(int i) {
		Speaker speaker = new Speaker();
		speaker.name = "名前"+i;
		speaker.belongTo ="所属"+i;
		speaker.profile = "プロフィール"+i;
		return speaker;
	}
	private Attendee createAttendee(int i) {
		Attendee attendee = new Attendee();
		attendee.email = "foo"+i+"@example.com";
		return attendee;
	}
	
	UUID sessionIdFixture;
	UUID attendeeIdFixture;
	UUID speakerIdFixture1;
	UUID speakerIdFixture2;

	void createSessions() {
		TestUtils.resetDb(ds);
    	
		TransactionStatus st = tm.getTransaction(null);
		
		List<Speaker> speakers = new ArrayList<>();
		for (int i=0; i<7; i++) {
			Speaker speaker = createSpeaker(i);
			speakerRepository.save(speaker);			
			speakers.add(speaker);
			if (i==0) {
				speakerIdFixture1 = speaker.id;
			}
			if (i==1) {
				speakerIdFixture2 = speaker.id;
			}
			
		}

		
		List<Session> sessions = new ArrayList<>();
		for (int i=0; i<10; i++) {
			Session session = createSession(i);
			sessionRepository.save(session);			
			if (i==0) {
				sessionIdFixture = session.id;
			}
			
			session.assignSpeakers(speakers);
			
			sessions.add(session);
		}
		
		
		
		for (int i=0; i<5; i++) {
			Attendee attendee = createAttendee(i);
			attendeeRepository.save(attendee);
			for (Session session : sessions) {
				session.attended(attendee);
			}		
			if (i==0) {
				attendeeIdFixture = attendee.id;
			}
		}
		
		
		tm.commit(st);			
	}
	
	
    @State("10 sessions with 7 speakers each")
    public void allSessions() {
    	createSessions();
    }
	
    @State("10 sessions with 7 speakers each with 5 attendees")
    public void allSessionsWithAttendee() {
    	createSessions();
    }
    
}
