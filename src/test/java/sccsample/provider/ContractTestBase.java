package sccsample.provider;

import java.util.UUID;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import io.restassured.RestAssured;
import jsug.portside.api.PortsideApplication;
import jsug.portside.api.controller.TestUtils;
import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.Speaker;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PortsideApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"admin.user=foo", "admin.pass=bar"})
public abstract class ContractTestBase {

	@LocalServerPort int port;

	@Autowired
    DataSource dataSource;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    SpeakerRepository speakerRepository;

    @Autowired
    AttendeeRepository attendeeRepository;

    @Autowired
    PlatformTransactionManager tm;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setup() {
        System.out.println("methodName="+testName.getMethodName());
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = this.port;
		
        TestUtils.resetDb(dataSource);

        TransactionStatus st = tm.getTransaction(null);

        switch (testName.getMethodName()) {        
        	case "validate_getSession":
        	case "validate_updateSession":
        	case "validate_assignSpeakers":
        	case "validate_deleteSession":
        	case "validate_attend":
        	case "validate_updateAttend":
        	case "validate_updateSpeaker":
        	case "validate_uploadSpeakerImage":
        		
	            Session session = SessionsFixture.createSession(0);
	            session.id = UUID.fromString("699f7072-e207-486f-94cd-3b259a4305ff");
	        	// JPAだと、@GeneratedValueのIDを、決め打ちの値でinsertするのは無理？
	            //sessionRepository.save(session);
	            JdbcTemplate jt = new JdbcTemplate(dataSource);
	            jt.update(
	            		"insert into session (description, speaker, title, id) values (?,?,?,?)",
	            		session.description, session.speaker, session.title, session.id);
	            Speaker speaker = SessionsFixture.createSpeaker(0);
	            speakerRepository.save(speaker);
	            session = sessionRepository.findById(session.id).get();
	            session.speakers.add(speaker);	            
	            
	            speaker = SessionsFixture.createSpeaker(0);
	            speaker.id =  UUID.fromString("799f7072-e207-486f-94cd-3b259a4305ff");
        		jt.update(
        				"insert into speaker (id, belong_to, name, profile) values (?,?,?,?)",
        				speaker.id,speaker.belongTo,speaker.name,speaker.profile);
	            speaker.id =  UUID.fromString("899f7072-e207-486f-94cd-3b259a4305ff");
        		jt.update(
        				"insert into speaker (id, belong_to, name, profile) values (?,?,?,?)",
        				speaker.id,speaker.belongTo,speaker.name,speaker.profile);
        		
	            session = SessionsFixture.createSession(1);
	            session.id = UUID.fromString("199f7072-e207-486f-94cd-3b259a4305ff");
	            jt.update(
	            		"insert into session (description, speaker, title, id) values (?,?,?,?)",
	            		session.description, session.speaker, session.title, session.id);
	            session.id = UUID.fromString("299f7072-e207-486f-94cd-3b259a4305ff");
	            jt.update(
	            		"insert into session (description, speaker, title, id) values (?,?,?,?)",
	            		session.description, session.speaker, session.title, session.id);
        		
        		Attendee attendee = SessionsFixture.createAttendee(0);
        		attendee.id = UUID.fromString("399f7072-e207-486f-94cd-3b259a4305ff");
        		jt.update(
        				"insert into attendee (id, email) values (?,?)",
        				attendee.id, attendee.email);
	            
        		break;
        		
        	default:
	            SessionsFixture.createData(
	                    sessionRepository, speakerRepository, attendeeRepository);
	            break;
        }

        tm.commit(st);


    }

}
