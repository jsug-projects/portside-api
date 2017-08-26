package jsug.portside.api.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.SessionAttendee;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SessionRepositoryTest {

	@Autowired
	TestEntityManager em;

	@Autowired
	SessionRepository target;

	@Before
	public void setup() {
		for (int i=0; i<10; i++) {
			Session session = createSession(i);
			System.out.println(session);
			em.persist(session);
			for (int j=0; j<5; j++) {
				SessionAttendee sa = createSessionAttendee(i, session);
				em.persist(sa);
			}
		}
		em.flush();
		em.clear();
	}
	public static Session createSession(int i) {
		Session session = new Session();
		session.title = "ダミーセッション"+i;
		session.speaker = "ダミースピーカー"+i;
		session.description = "ダミー概要"+i;
		return session;		
	}
	
	public SessionAttendee createSessionAttendee(int i, Session session) {
		SessionAttendee sa = new SessionAttendee();
		sa.session = session;
		return sa;
	}
	
	
	public static SessionWithAttendeeCountDto createSessionDto(int i) {
		SessionWithAttendeeCountDto dto = new SessionWithAttendeeCountDto();
		dto.session = createSession(i);
		dto.attendeeCount = i;
		return dto;
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

}
