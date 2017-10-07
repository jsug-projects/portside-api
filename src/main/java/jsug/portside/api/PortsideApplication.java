package jsug.portside.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(Source.class)
public class PortsideApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx  = SpringApplication.run(PortsideApplication.class, args);
		ctx.getBean(DummyDataCreator.class).createDummyData();
	}
	
	@Bean
	public DummyDataCreator dummyDataCreator() {
		return new DummyDataCreator();
	}


	
}
