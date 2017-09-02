package jsug.portside.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "flyway.baseline-on-migrate=true")
public class SessionControllerTestContainer {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    String baseUrl;
    
    UUID attendeeIdFixture;
    
	@Before
	public void setUp() throws Exception {
		baseUrl = "http://localhost:"+port;
		
		List<Session> sessions = new ArrayList<>();
		for (int i=0; i<10; i++) {
			Session session = createSession(i);
			String location = restTemplate.postForLocation(baseUrl+"/sessions", session).toString();
			session.id = UUID.fromString(location.substring(location.lastIndexOf('/')+1));
			sessions.add(session);
		}
		
		
		for (int i=0; i<5; i++) {
			AttendRequestForm form = new AttendRequestForm();
			form.email = "foo"+i+"@example.com";
			for (Session session : sessions) {
				form.ids.add(session.id);
			}		
			String location = restTemplate.postForLocation(baseUrl+"/attendees", form).toString();
			
			if (i==0) {
				attendeeIdFixture = UUID.fromString(location.substring(location.lastIndexOf('/')+1));
			}
		}
		
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

	@Autowired
	DataSource ds;
	@After
	public void tearDown() {
		TestUtils.resetDb(ds);
	}
	
	@Test
	public void testGetAllSessions() throws Exception {
		
		Session[] sessions = restTemplate.getForObject(baseUrl+"/sessions", Session[].class);
		assertThat(sessions.length, is(10));
		
	}
	@Test
	public void testGetAllSessionWithAttendeeCounts() throws Exception {
		SessionWithAttendeeCountDto[] sessions = restTemplate.getForObject(baseUrl+"/sessionsWithAttendeeCount", SessionWithAttendeeCountDto[].class);
		assertThat(sessions.length, is(10));
		assertThat(sessions[0].attendeeCount, is(5));
	}


}
