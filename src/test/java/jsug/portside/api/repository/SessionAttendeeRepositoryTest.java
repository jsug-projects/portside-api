package jsug.portside.api.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.SessionAttendee;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SessionAttendeeRepositoryTest {

	@Autowired
	TestEntityManager em;

	@Autowired
	SessionAttendeeRepository target;
	
	Attendee attendeeFixture;
	
	@Before
	public void setup() {
		for (int i=0; i<2; i++) {
			Attendee attendee = createAttendee(i);
			em.persist(attendee);
			for (int j=0; j<5; j++) {
				Session session = createSession(j);
				SessionAttendee sa = createSessionAttendee(j, session, attendee);
				em.persist(sa);
			}
			if (i==0) {
				attendeeFixture = attendee;
			}
		}		
		
		
		em.flush();
		em.clear();
	}
	private Attendee createAttendee(int i) {
		Attendee attendee = new Attendee();
		attendee.email = "foo"+i+"@example.com";
		return attendee;		
	}
	private Session createSession(int i) {
		Session session = new Session();
		session.title = "ダミーセッション"+i;
		session.speaker = "ダミースピーカー"+i;
		session.description = "ダミー概要"+i;
		return session;		
	}
	public SessionAttendee createSessionAttendee(int i, Session session, Attendee attendee) {
		SessionAttendee sa = new SessionAttendee();
		sa.assign(session, attendee);
		return sa;
	}

	
	@Test
	public void testFindByAttendeeId() throws Exception {
		List<SessionAttendee> list = target.findByAttendeeId(attendeeFixture.id);
		assertThat(list.size(), is(5));
	}

}
