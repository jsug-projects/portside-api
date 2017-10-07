package jsug.portside.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;

@Service
public class MailTemplateService {

	@Autowired
	SpringTemplateEngine templateEngine;
	
	
	public String createThankyouMailSubject() {
		return "【Spring Fest2017】ご協力ありがとうございました。";
	}
	
	public String createThankyouMailBody(Attendee attendee, List<Session> sessions) {
		Context context = new Context();
		context.setVariable("attendee", attendee);
		context.setVariable("sessions", sessions);
		return templateEngine.process("thankyou-mail", context);
	}
	
	
	
	
	
}
