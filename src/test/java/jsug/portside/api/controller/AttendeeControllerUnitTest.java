package jsug.portside.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.mockito.internal.matchers.StartsWith;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.ResponseEntity;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.service.MailTemplateService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AttendeeControllerUnitTest {

	@Tested
	AttendeeController target;
	
	@Injectable
	SessionRepository sessionRepository;
	
	@Injectable
	AttendeeRepository attendeeRepository;
	
	@Injectable Source source;
	
	@Injectable MailTemplateService mailTemplateService;
	
	@Test
	public void testUpdateAttend(@Injectable Session s1, @Injectable Session s2) {
		new Expectations() {{
			sessionRepository.findByAttendeesId((UUID)any);
			List<Session> list = new ArrayList<>();
			list.add(s1);
			list.add(s2);
			result = list;
						
			s1.unAttended((Attendee)any);
			s1.attended((Attendee)any);
			s2.unAttended((Attendee)any);
			s2.attended((Attendee)any);
		}};
		
		List<Session> list = new ArrayList<>();
		list.add(s1);
		list.add(s2);
		target.updateAttend(new Attendee(), list);
		
	}
	
	@Test
	public void testAttend(@Injectable Session s1, @Injectable Session s2) {
		new Expectations() {{
			attendeeRepository.findByEmail((String)any);
			result = null;
			attendeeRepository.save((Attendee)any);
			sessionRepository.findAll((List<UUID>)any);
			List<Session> list = new ArrayList<>();
			list.add(s1);
			list.add(s2);
			result = list;
						
			s1.attended((Attendee)any);
			s2.attended((Attendee)any);
		}};
		
		AttendRequestForm form = new AttendRequestForm();
		List<UUID> ids = new ArrayList<>();
		ids.add(UUID.randomUUID());
		ids.add(UUID.randomUUID());
		form.ids = ids;
		ResponseEntity<?> res = target.attend(form, "http://xxx");
		assertThat(res.getHeaders().getLocation().toString(), startsWith("http://xxx"));
		
	}
	
	@Test
	public void testAttendAttendeeExists(@Injectable Session s1, @Injectable Session s2) {
		new Expectations(target) {{
			attendeeRepository.findByEmail((String)any);
			result = new Attendee();
			target.updateAttend((Attendee)any, (Iterable<Session>)any);
		}};
		target.attend(new AttendRequestForm(), "http://xxx");
		
	}
	
	
	
	
	
	
}
