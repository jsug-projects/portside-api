package sccsample.provider.ui;

import jsug.portside.api.PortsideApplication;
import jsug.portside.api.controller.TestUtils;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import sccsample.SessionsFixture;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PortsideApplication.class)
@AutoConfigureMockMvc
public abstract class UiContractTestBase {

	@Autowired
	MockMvc mvc;

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
        RestAssuredMockMvc.mockMvc(mvc);
        TestUtils.resetDb(dataSource);

        switch (testName.getMethodName()) {
            case "validate_sessions":
                SessionsFixture.createData(tm,
                        sessionRepository, speakerRepository, attendeeRepository);
                break;
            default:
                System.out.println("!!!!! not match !!!!! methodName="+testName.getMethodName());
                break;
        }
    }

}
