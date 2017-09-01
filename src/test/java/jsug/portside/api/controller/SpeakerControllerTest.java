package jsug.portside.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jsug.portside.api.entity.Speaker;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpeakerControllerTest {

	@Autowired
	private MockMvc mvc;
	ObjectMapper om = new ObjectMapper();
	UUID speakerIdFixture; 
	
	@Before
	public void setup() throws Exception {
		for (int i=0; i<10; i++) {
			Speaker speaker = new Speaker();
			speaker.name = "名前" + i;
			speaker.profile = "プロフィール" + i;
			speaker.belongTo = "所属" + i;
			
			String json = om.writeValueAsString(speaker);
			
			String location = mvc.perform(post("/speakers").content(json).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
			if (i==0) {
				speakerIdFixture = TestUtils.getFromLocation(location);
			}
		}
	}
	
	@Autowired
	DataSource ds;
	
	@After
	public void teardown() {
		TestUtils.resetDb(ds);
	}
	
	
	@Test
	public void testRegister() throws Exception {
		
		Speaker speaker = new Speaker();
		speaker.name = "名前";
		speaker.profile = "プロフィール";
		speaker.belongTo = "所属";
		
		String json = om.writeValueAsString(speaker);
		
		String location = mvc.perform(post("/speakers").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
		UUID id = TestUtils.getFromLocation(location);
		
		mvc.perform(get("/speakers/"+id))
		.andExpect(jsonPath("$.name", is("名前")));
				
	}

	@Test
	public void testUpdate() throws Exception {
		Speaker speaker = new Speaker();
		speaker.name = "updated";
		String json = om.writeValueAsString(speaker);
		mvc.perform(put("/speakers/"+speakerIdFixture).content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
		mvc.perform(get("/speakers/"+speakerIdFixture))
		.andExpect(jsonPath("$.name", is("updated")));
		
	}
	
	@Test
	public void testUpdateInvalidArgs() throws Exception {
		Speaker speaker = new Speaker();
		speaker.name = "";
		String json = om.writeValueAsString(speaker);
		mvc.perform(put("/speakers/"+speakerIdFixture).content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is4xxClientError());
		
	}


	@Test
	public void testGets() throws Exception {
		mvc.perform(get("/speakers"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(10)));
	}


	@Test
	public void testRegisterImage() throws Exception {
		MockMultipartFile file = new MockMultipartFile("data", "filename.txt", 
				"image/jpg", "foo".getBytes());
		
		mvc.perform(fileUpload("/speakers/"+speakerIdFixture+"/image").file(file))
		.andExpect(status().isCreated());
		
		mvc.perform(get("/speakers/"+speakerIdFixture+"/image"))
		.andExpect(status().isOk())
		.andExpect(content().bytes("foo".getBytes()));
		
		
	}
	
	
	
	

}
