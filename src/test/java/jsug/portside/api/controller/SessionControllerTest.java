package jsug.portside.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import jsug.portside.api.dto.AssignSpeakersForm;
import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.Speaker;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

	@Autowired
	private MockMvc mvc;

	UUID sessionIdFixture;
	UUID attendeeIdFixture;
	UUID speakerIdFixture1;
	UUID speakerIdFixture2;
	
	ObjectMapper om = new ObjectMapper();
	
	
	@Autowired
	SessionRepository sessionRepository;
	
	@Autowired
	SpeakerRepository speakerRepository;
	
	@Autowired
	AttendeeRepository attendeeRepository;

	@Autowired
	PlatformTransactionManager tm;

	@Before
	public void setUp() throws Exception {
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

	@Autowired
	DataSource ds;
	
	@After
	public void tearDown() {
		TestUtils.resetDb(ds);
	}
	
	@Test
	public void testGetAllSessions() throws Exception {
		mvc.perform(get("/sessions"))
		.andExpect(jsonPath("$", hasSize(10)))
		.andExpect(jsonPath("$[0].speakers", hasSize(7)));
	}
	@Test
	public void testGetAllSessionWithAttendeeCounts() throws Exception {
		String body = mvc.perform(get("/sessions/withAttendeeCount"))
		.andExpect(jsonPath("$", hasSize(10)))
		.andExpect(jsonPath("$[0].attendeeCount", is(5)))
		.andExpect(jsonPath("$[0].session.speakers", hasSize(7))).andReturn().getResponse().getContentAsString();
		System.out.println(body);
	}
	
	@Test
	public void testRegisterSession() throws Exception {
		Session session = createSession(0);
		session.speakers.add(createSpeaker(0));
		session.speakers.add(createSpeaker(1));
		session.speakers.add(createSpeaker(2));
		
		String json = om.writeValueAsString(session);		
		System.out.println(json);
		String location = mvc.perform(post("/sessions").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
		UUID id = TestUtils.getFromLocation(location);
		
		mvc.perform(get("/sessions/"+id))
		.andExpect(jsonPath("$.title", is("ダミーセッション0")))
		.andExpect(jsonPath("$.speakers", hasSize(3)));
		;
		
	}
	
	
	@Test
	public void testUpdateSession() throws Exception {
		Session session = new Session();
		session.title = "updated";
		session.description = "updatedDescription";
		
	
		session.speakers.add(createSpeaker(0));
		session.speakers.add(createSpeaker(1));
		
		String json = om.writeValueAsString(session);		
		
		mvc.perform(put("/sessions/"+sessionIdFixture.toString())
				.content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
		mvc.perform(get("/sessions/"+sessionIdFixture.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title", is("updated")))
			.andExpect(jsonPath("$.description", is("updatedDescription")))
			.andExpect(jsonPath("$.speakers", hasSize(2)))
			;
			
		mvc.perform(get("/speakers/"+speakerIdFixture1))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		mvc.perform(get("/speakers/"+speakerIdFixture2))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
			
		
	}
	@Test
	public void testAssignSpeakers() throws Exception {
		
		AssignSpeakersForm form = new AssignSpeakersForm();
		form.speakerIds.add(speakerIdFixture1);
		form.speakerIds.add(speakerIdFixture2);
		String json = om.writeValueAsString(form);
		mvc.perform(put("/sessions/"+sessionIdFixture+"/assignSpeakers").content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
		mvc.perform(get("/sessions/"+sessionIdFixture))
		.andExpect(jsonPath("$.speakers", hasSize(2)));
		
		
		form = new AssignSpeakersForm();
		form.speakerIds.add(speakerIdFixture1);
		
		json = om.writeValueAsString(form);
		mvc.perform(put("/sessions/"+sessionIdFixture+"/assignSpeakers").content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
		mvc.perform(get("/sessions/"+sessionIdFixture))
		.andExpect(jsonPath("$.speakers", hasSize(1)));
		
		
	}
	@Test
	public void testGetSession() throws Exception {
		mvc.perform(get("/sessions/"+sessionIdFixture))
		.andExpect(jsonPath("$.title", is("ダミーセッション0")));
	}

	@Test
	public void testDeleteSession() throws Exception {
		mvc.perform(delete("/sessions/"+sessionIdFixture))
		.andExpect(status().isNoContent());
		
		mvc.perform(get("/sessions/"+sessionIdFixture))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		mvc.perform(get("/speakers/"+speakerIdFixture1))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		mvc.perform(get("/speakers/"+speakerIdFixture2))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		
	}
	

}
