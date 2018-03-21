package pactsample.consumer.ui_api_contract;


import static io.pactfoundation.consumer.dsl.LambdaDsl.*;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactSpecVersion;
import au.com.dius.pact.model.RequestResponsePact;

//pactファイルのinteractionsの要素の数が２つ出来てほしいが４つ出来るなー。
public class SessionContractConsumerTest {

    @Rule
    public PactProviderRuleMk2  provider = new PactProviderRuleMk2("portside-api", PactSpecVersion.V2, this);
    
    static <T> String readFileFromSamePackage(String fileName, Class<T> clazz) {
    	try {
    		return IOUtils.toString(clazz.getResourceAsStream(fileName), StandardCharsets.UTF_8.name());
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
    DslPart createSessions(boolean withCount) {
    	return newJsonArray((rootArray)-> {
    		for (int i=0; i<10; i++) {
    			rootArray.object((session->{
    				session
    				.uuid("id")
    				.stringType("title", "description", "speaker")
    				.array("speakers", (speakers)->{
    					for (int j=0; j<7; j++) {
    						speakers.object((speaker)->{	    						
	    						speaker
	    						.uuid("id")
	    						.stringType("name", "belongTo", "profile")
	    						.nullValue("imageUrl");//anyなTypeでマッチングしたいが、術がないみたい
	    					});
    					}
    				});
    				if (withCount == true) {
    					session.numberType("attendeeCount");
    				}
    			}));
    		}
    	}).build();    	
    }
    
    
    @Pact(consumer="portside-ui")
    public RequestResponsePact createFragmentSessions(PactDslWithProvider builder) {
        return builder
                .given("10 sessions with 7 speakers each")
                .uponReceiving("a request for all sessions")
                    .path("/sessions")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body(createSessions(false))
                .toPact();
    }    

    
    @Pact(consumer="portside-ui")
    public RequestResponsePact createFragmentSessionsWithActtendeeCount(PactDslWithProvider builder) {
    	
        return builder
                .given("10 sessions with 7 speakers each with 5 attendees")
                .uponReceiving("a request for all sessions with attendee count")
                    .path("/sessions/withAttendeeCount")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body(createSessions(true))
                .toPact();
    }    
    
    
    @Test
    @PactVerification(fragment="createFragmentSessions")
    public void testSessions(){
    	//とりあえずpactファイルを作りたいので、単にAPIを呼び出すだけ
    	new RestTemplate().getForObject(provider.getUrl()+"/sessions", String.class);
    }
    
    @Test
    @PactVerification(fragment="createFragmentSessionsWithActtendeeCount")
    public void testSessionsWithAttendeeCount(){
    	//とりあえずpactファイルを作りたいので、単にAPIを呼び出すだけ
    	new RestTemplate().getForObject(provider.getUrl()+"/sessions/withAttendeeCount", String.class);
    }
}