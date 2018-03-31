package sccsample.consumer.ui;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = {"jsug.portside:portside-api:+:stubs:8999"},
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@DirtiesContext
public class UiSessionContractTest {
    @Configuration
    static class Config {

    }

    @Test
    public void foo() {
        RestTemplate rt = new RestTemplate();
        rt.getForObject("http://localhost:8999/sessions", String.class);
    }

}
