package jsug.portside.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import jsug.portside.api.config.ThymeleafConfig;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class MailTemplateServiceTest {
	
	@Configuration
	@Import(ThymeleafConfig.class)
	public static class Config {
		@Bean
		public MailTemplateService service() {
			return new MailTemplateService();
		}
	}
	
	@Autowired
	MailTemplateService target;
	
	@Test
	public void testCreateThankyouMailBody() throws Exception {
		Attendee attendee = new Attendee();
		attendee.id = UUID.randomUUID();
		
		List<Session> sessions = new ArrayList<>();
		Session session = new Session();
		session.title = "タイトル１";
		sessions.add(session);
		session = new Session();
		session.title = "タイトル２";
		sessions.add(session);
		
		String result = target.createThankyouMailBody(attendee, sessions);
		System.out.println(result);
		assertThat(result, containsString(attendee.id.toString()));
		assertThat(result, containsString("タイトル１"));
		assertThat(result, containsString("タイトル２"));
		
		
	}

}
