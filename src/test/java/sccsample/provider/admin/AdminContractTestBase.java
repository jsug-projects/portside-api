package sccsample.provider.admin;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import io.restassured.RestAssured;
import jsug.portside.api.PortsideApplication;
import jsug.portside.api.controller.TestUtils;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;
import sccsample.SessionsFixture;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PortsideApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AdminContractTestBase {

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

        switch (testName.getMethodName()) {
            case "validate_sessionsWithAttendeeCount":
                SessionsFixture.createData(tm,
                        sessionRepository, speakerRepository, attendeeRepository);
                break;
            case "validate_registerSession":
                SessionsFixture.createData(tm,
                        sessionRepository, speakerRepository, attendeeRepository);
                break;
            default:
                System.out.println("!!!!! not match !!!!! methodName="+testName.getMethodName());
                break;
        }
    }

}
