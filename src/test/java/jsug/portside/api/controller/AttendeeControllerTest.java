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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.dto.UpdateAttendRequestForm;
import jsug.portside.api.entity.Session;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AttendeeControllerTest {

	@Autowired
	private MockMvc mvc;

	UUID sessionIdFixture1;
	UUID sessionIdFixture2;
	UUID attendeeIdFixture;
	String emailFixture;
	ObjectMapper om = new ObjectMapper();
	
	@Before
	public void setUp() throws Exception {
		
		List<Session> sessions = new ArrayList<>();
		for (int i=0; i<10; i++) {
			Session session = createSession(i);
			String json = om.writeValueAsString(session);		
			String location = mvc.perform(post("/sessions").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
			session.id = TestUtils.getFromLocation(location);
			
			if (i==0) {
				sessionIdFixture1 = session.id;
			}
			if (i==1) {
				sessionIdFixture2 = session.id;
			}
			
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
			
			if (i==0) {
				attendeeIdFixture = TestUtils.getFromLocation(location);
				emailFixture = form.email;
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
	

	@Autowired
	DataSource ds;
	
	@After
	public void tearDown() {
		TestUtils.resetDb(ds);
	}
	
	@Test
	public void testGetAllAttendees() throws Exception {
		mvc.perform(get("/attendees"))
		.andExpect(jsonPath("$", hasSize(5)));
	}
	
	@Test
	public void testAttend() throws Exception {
		
		AttendRequestForm form = new AttendRequestForm();
		form.email = "foo999@example.com";
		form.ids.add(sessionIdFixture1);
				
		String json = om.writeValueAsString(form);				
		String location = mvc.perform(post("/attendees")
				.content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
		
		String attendeeId = location.substring(location.lastIndexOf('/')+1);
		
		mvc.perform(get("/attendees/"+attendeeId+"/sessions"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)));
		
	}

	@Test
	public void testAttendExists() throws Exception {
		AttendRequestForm form = new AttendRequestForm();
		form.email = emailFixture;
		form.ids.add(sessionIdFixture1);
		form.ids.add(sessionIdFixture2);
		
		String json = om.writeValueAsString(form);				
		String location = mvc.perform(post("/attendees")
				.content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
		
		mvc.perform(get("/attendees/"+attendeeIdFixture+"/sessions"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)));
		
	}
	@Test
	public void testUpdateAttendById() throws Exception {
		UpdateAttendRequestForm form = new UpdateAttendRequestForm();
		form.ids.add(sessionIdFixture1);
		form.ids.add(sessionIdFixture2);

		String json = om.writeValueAsString(form);				
		mvc.perform(put("/attendees/"+attendeeIdFixture).content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
		mvc.perform(get("/attendees/"+attendeeIdFixture+"/sessions"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)));
		
	}

}
