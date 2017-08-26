package jsug.portside.api.repository;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Session;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Ignore
public class SessionRepositoryTest {

	@Autowired
	TestEntityManager em;

	@Autowired
	SessionRepository target;

	@Before
	public void setup() {
		for (int i=0; i<10; i++) {
			Session session = createSession(i);
			em.persist(session);
		}
	}
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
	
	
//	@Test
//	public void testFindAll() throws Exception {
//		List<Session> list = target.
//		assertThat(list.size(), is(10));
//	}

}
