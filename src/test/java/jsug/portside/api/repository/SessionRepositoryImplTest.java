package jsug.portside.api.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(properties = "flyway.baseline-on-migrate=true")
public class SessionRepositoryImplTest {

	@Autowired
	TestEntityManager em;

	@Autowired
	SessionRepository target;

	Attendee attendeeFixture;
	
	@Before
	public void setup() {
		List<Session> sessions = new ArrayList<>();
		for (int i=0; i<10; i++) {
			Session session = createSession(i);
			em.persist(session);
			sessions.add(session);
		}
		List<Attendee> attendees = new ArrayList<>();
		for (int i=0; i<5; i++) {
			Attendee attendee = createAttendee(i);
			em.persist(attendee);
			attendees.add(attendee);
			
			if (i==0) {
				attendeeFixture = attendee;
			}
		}
		for (Session session : sessions) {
			for (Attendee attendee: attendees) {
				session.attendees.add(attendee);
			}
		}
		
		em.flush();
		em.clear();
	}
	private Session createSession(int i) {
		Session session = new Session();
		session.title = "ダミーセッション"+i;
		session.speaker = "ダミースピーカー"+i;
		session.description = "ダミー概要"+i;
		return session;		
	}
	
	private Attendee createAttendee(int i) {
		Attendee attendee = new Attendee();
		attendee.email = "foo"+i+"@example.com";
		return attendee;
	}
		
	
	@Test
	public void testFindAll() throws Exception {
		Iterable<Session> obj = target.findAll();
		List<Session> list = Lists.newArrayList(obj);
		assertThat(list.size(), is(10));
	}
	@Test
	public void testFindSessionsWithAttendeeCount() throws Exception {
		List<SessionWithAttendeeCountDto> list = target.findSessionsWithAttendeeCount();
		assertThat(list.size(), is(10));
		assertThat(list.get(0).attendeeCount, is(5));
		assertThat(list.get(1).attendeeCount, is(5));
		
	}

	@Test
	public void testFindByAttendeesId() throws Exception {
		List<Session> list = target.findByAttendeesId(attendeeFixture.id);
		assertThat(list.size(), is(10));
	}
	
}
