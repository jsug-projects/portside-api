package jsug.portside.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(Source.class)
public class PortsideApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(PortsideApplication.class, args);
	}
	
	
	@Bean
	@Profile("needDummyData")
	public CommandLineRunner dummyDataCreate(DummyDataCreator dummyDataCreator) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				dummyDataCreator.createDummyData();
			}
		};
	}
	
	
	@Bean
	@Profile("needDummyData")
	public DummyDataCreator dummyDataCreator() {
		return new DummyDataCreator();
	}


	
}
