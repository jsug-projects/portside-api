package sccsample.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jsug.portside.api.controller.SessionController;
import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.Speaker;
import mockit.Expectations;
import mockit.Injectable;

public abstract class ContractTestBaseControllerMock {

	@Injectable
	SessionController sessionController;

    @Before
    public void setup() {
    	new Expectations() {{
        	List<SessionWithAttendeeCountDto> list = new ArrayList<>();
        	for (int i=0; i<10; i++) {
        		list.add(createSessionWithAttendeeCountDto(i));
        	}
        	sessionController.getAllSessionWithAttendeeCounts();
        	result = list;
		}};
    	
    	RestAssuredMockMvc.standaloneSetup(sessionController);
    }
    
    SessionWithAttendeeCountDto createSessionWithAttendeeCountDto(int idx) {
    	SessionWithAttendeeCountDto sessionWithAttendeeCountDto = new SessionWithAttendeeCountDto();
    	sessionWithAttendeeCountDto.attendeeCount = 10;
    	sessionWithAttendeeCountDto.session = createSession(idx, 7);    	
    	return sessionWithAttendeeCountDto;    	
    }
    
    Session createSession(int idx, int speakerSize) {
    	Session session = new Session();
    	session.id = UUID.randomUUID();
    	session.title = "title01";
    	session.description = "desc001";
    	session.speaker = "speaker001";
    	for (int i=0; i<speakerSize; i++) {
    		session.speakers.add(createSpeaker(i));
    	}
    	return session;    	
    }
    
    Speaker createSpeaker(int idx) {
    	Speaker speaker = new Speaker();
    	speaker.belongTo = "belong"+idx;
    	speaker.id = UUID.randomUUID();
    	speaker.name = "name"+idx;
    	speaker.profile = "proflie"+idx;
    	return speaker;
    }
    

}
