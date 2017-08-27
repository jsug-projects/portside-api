package jsug.portside.api.entity;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class SessionTest {

	Session target;
	
	Attendee attendeeFixture;
	
	@Before
	public void setup() {
		target = new Session();
		for (int i=0; i<10; i++) {
			Attendee attendee = createAttendee(i);
			target.attendees.add(attendee);
			if (i == 5) {
				attendeeFixture = attendee;
			}
		}
	}
	
	private Attendee createAttendee(int i) {
		Attendee attendee = new Attendee();
		attendee.id = UUID.randomUUID();
		attendee.email = "foo"+i+"@example.com";
		return attendee;
	}
	
	@Test
	public void testAttended() throws Exception {
		Attendee attendee = createAttendee(0);
		target.attended(attendee);
		
		assertThat(target.attendees.size(), is(11));
		
	}

	@Test
	public void testUnAttended() throws Exception {
		target.unAttended(attendeeFixture);
		assertThat(target.attendees.size(), is(9));
		
		for(Attendee attendee : target.attendees) {
			if (attendee.id.equals(attendeeFixture.id)) {
				fail();
			}
		}
		
	}

}
