package jsug.portside.api.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Session;

public class SessionRepositoryImpl implements SessionRepositoryCustom {

	@Autowired
	SessionRepository sessionRepository; 
	
	@Autowired
	EntityManager em;
	
	@Override
	public List<SessionWithAttendeeCountDto> findSessionsWithAttendeeCount() {
				
		List<Session> sessions = Lists.newArrayList(sessionRepository.findAll());
		
		List<SessionWithAttendeeCountDto> list = new ArrayList<>();
		for (Session session : sessions) {
			SessionWithAttendeeCountDto  dto = new SessionWithAttendeeCountDto();
			dto.session = session;
			dto.attendeeCount = session.attendees.size();
			list.add(dto);
		}
			  
		return list;
		
	}
	

}
