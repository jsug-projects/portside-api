package jsug.portside.api.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class PortsideControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Before
	public void setUp() {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
//				.apply(documentationConfiguration(this.restDocumentation).uris().withPort(8083))
//				.build();
//		dummyDataCreator.createDummyData();
//		eventFixture = dummyDataCreator.createdEvents.get(0);
	}
	
	@Test
	public void testGetAllSessions() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testGetAllSessionWithAttendeeCounts() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testRegisterSession() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testUpdateSession() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testLogin() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testLoginError() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

}
