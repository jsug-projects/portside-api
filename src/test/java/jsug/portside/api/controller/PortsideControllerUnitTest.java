package jsug.portside.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

public class PortsideControllerUnitTest {

	@Tested
	PortsideController target;
	
	@Injectable
	SessionRepository sessionRepository;
	
	@Injectable
	AttendeeRepository attendeeRepository;
	
	
	@Test
	public void testUpdateAttend(@Injectable Session s1, @Injectable Session s2) {
		new Expectations() {{
			sessionRepository.findByAttendeesId((UUID)any);
			List<Session> list = new ArrayList<>();
			list.add(s1);
			list.add(s2);
			result = list;
						
			sessionRepository.findAll((List<UUID>)any);
			result = list;
			
			s1.unAttended((Attendee)any);
			s1.attended((Attendee)any);
			s2.unAttended((Attendee)any);
			s2.attended((Attendee)any);
		}};
		
		List<UUID> ids = new ArrayList<>();
		ids.add(UUID.randomUUID());
		ids.add(UUID.randomUUID());
		target.updateAttend(new Attendee(), ids);
		
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
		target.attend(form);
		
	}
	
	@Test
	public void testAttendAttendeeExists(@Injectable Session s1, @Injectable Session s2) {
		new Expectations(target) {{
			attendeeRepository.findByEmail((String)any);
			result = new Attendee();
			target.updateAttend((Attendee)any, (List<UUID>)any);
		}};
		target.attend(new AttendRequestForm());
		
	}
	
	
	
	
	
	
}
