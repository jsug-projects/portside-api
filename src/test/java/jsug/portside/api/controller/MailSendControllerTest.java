package jsug.portside.api.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.entity.Session;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MailSendControllerTest {
	@Autowired
	MessageCollector messageCollector;
	@Autowired
	Source source;

	@Autowired
	private MockMvc mvc;

	UUID sessionIdFixture1;
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
			
			sessions.add(session);
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
	
	
//	@Test
//	public void hi() throws Exception {
//		restTemplate.postForObject("/", null, Void.class);
//		Message message = messageCollector.forChannel(source.output()).poll(2,
//				TimeUnit.SECONDS);
//		assertThat(message.getPayload()).isEqualTo("{\"text\":\"こんにちは!!\"}");
//	}
	
	@Test
	public void testAttend() throws Exception {
		
		AttendRequestForm form = new AttendRequestForm();
		form.email = "kouhei.toki@gmail.com";
		form.ids.add(sessionIdFixture1);
				
		String json = om.writeValueAsString(form);				
		String location = mvc.perform(post("/attendees")
				.content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");

		Message message = messageCollector.forChannel(source.output()).poll(2,
				TimeUnit.SECONDS);
		assertThat((String)message.getPayload(), containsString("アンケートのご協力ありがとうございました"));
		
		
	}
	

}