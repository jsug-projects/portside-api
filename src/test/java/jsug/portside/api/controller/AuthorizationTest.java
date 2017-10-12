package jsug.portside.api.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.dto.UpdateAttendRequestForm;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.Speaker;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"admin.user=foo", "admin.pass=bar"})
public class AuthorizationTest {

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
	public void testSession() throws Exception {
		mvc.perform(get("/sessions")).andExpect(status().isOk());
		mvc.perform(get("/sessions/"+this.sessionIdFixture)).andExpect(status().isOk());
		mvc.perform(get("/sessions/withAttendeeCount")).andExpect(status().isForbidden());
		mvc.perform(post("/sessions")).andExpect(status().isForbidden());
		mvc.perform(put("/sessions/"+this.sessionIdFixture)).andExpect(status().isForbidden());
		mvc.perform(delete("/sessions/"+this.sessionIdFixture)).andExpect(status().isForbidden());
		mvc.perform(options("/sessions")).andExpect(status().isOk());


		Session session = createSession(0);
		session.speakers.add(createSpeaker(0));
		session.speakers.add(createSpeaker(1));
		session.speakers.add(createSpeaker(2));
		String json = om.writeValueAsString(session);
		System.out.println(json);
		mvc.perform(post("/sessions").with(httpBasic("foo","bar")).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());


	}
	@Test
	public void testAttendee() throws Exception {

		mvc.perform(get("/attendees")).andExpect(status().isForbidden());
		mvc.perform(get("/attendees/"+this.attendeeIdFixture+"/sessions")).andExpect(status().isOk());

		AttendRequestForm form = new AttendRequestForm();
		form.email = "foo999@example.com";
		form.ids.add(this.sessionIdFixture);
		String json = om.writeValueAsString(form);
		mvc.perform(post("/attendees").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		UpdateAttendRequestForm uform = new UpdateAttendRequestForm();
		uform.ids.add(this.sessionIdFixture);
		json = om.writeValueAsString(uform);
		mvc.perform(put("/attendees/"+attendeeIdFixture).content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

		mvc.perform(options("/attendees")).andExpect(status().isOk());
	}
	@Test
	public void testSpeaker() throws Exception {

		mvc.perform(get("/speakers")).andExpect(status().isForbidden());
		mvc.perform(get("/speakers/"+this.speakerIdFixture1)).andExpect(status().isForbidden());
		mvc.perform(get("/speakers/"+this.speakerIdFixture1+"/image")).andExpect(status().isOk());
		mvc.perform(post("/speakers")).andExpect(status().isForbidden());
		mvc.perform(put("/speakers/"+this.attendeeIdFixture)).andExpect(status().isForbidden());
		mvc.perform(post("/speakers/"+this.speakerIdFixture1+"/image")).andExpect(status().isForbidden());
		mvc.perform(options("/speakers")).andExpect(status().isOk());


	}


}
