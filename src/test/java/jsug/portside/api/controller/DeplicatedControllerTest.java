package jsug.portside.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeplicatedControllerTest {

	@Autowired
	private MockMvc mvc;

	ObjectMapper om = new ObjectMapper();
	
	@Before
	public void setUp() throws Exception {
		
		List<Session> sessions = new ArrayList<>();
		for (int i=0; i<10; i++) {
			Session session = createSession(i);
			String json = om.writeValueAsString(session);		
			String location = mvc.perform(post("/sessions").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
			session.id = TestUtils.getFromLocation(location);
			
			sessions.add(session);
		}
		
		
		for (int i=0; i<5; i++) {

			AttendRequestForm form = new AttendRequestForm();
			form.email = "foo"+i+"@example.com";
			for (Session session : sessions) {
				form.ids.add(session.id);
			}		
			String json = om.writeValueAsString(form);		
			String location = mvc.perform(post("/attendees")
					.content(json).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
			
		}
		
	}
	private Session createSession(int i) {
		Session session = new Session();
		session.title = "ダミーセッション"+i;
		session.speaker = "ダミースピーカー"+i;
		session.description = "ダミー概要"+i;
		return session;		
	}
	

	@Autowired
	DataSource ds;
	
	@After
	public void tearDown() {
		TestUtils.resetDb(ds);
	}
	
	@Test
	public void testGetAllSessionWithAttendeeCounts() throws Exception {
		mvc.perform(get("/sessionsWithAttendeeCount"))
		.andExpect(jsonPath("$", hasSize(10)))
		.andExpect(jsonPath("$[0].attendeeCount", is(5)));
	}

}
