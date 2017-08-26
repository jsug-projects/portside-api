package jsug.portside.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jsug.portside.api.dto.AttendRequestForm;
import jsug.portside.api.dto.SessionWithAttendeeCountDto;
import jsug.portside.api.entity.Session;

@RestController
//@CrossOrigin
public class PortsideApiController {

	@GetMapping("/sessions")
	public List<Session> getAllSessions() {
		
		List<Session> list = new ArrayList<>();
		for (int i=0; i<10; i++) {
			list.add(DummyData.createSession(i));
		}
		return list;
	}
	@GetMapping("/sessionsWithAttendeeCount")
	public List<SessionWithAttendeeCountDto> getAllSessionWithAttendeeCounts() {
		
		List<SessionWithAttendeeCountDto> list = new ArrayList<>();
		for (int i=0; i<10; i++) {
			list.add(DummyData.createSessionDto(i));
		}
		return list;
	}
	
	
	
	@PostMapping("/sessions")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerSession(@Validated @RequestBody Session session) {
		
		System.out.println("登録しました="+session);
		
		
	}

	@PutMapping("/sessions/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateSession(@Validated @RequestBody Session session) {

		System.out.println("更新しました="+session);

		
	}
	
	
	@PostMapping("/attendees")
	@ResponseStatus(HttpStatus.CREATED)
	public void attend(@Validated @RequestBody AttendRequestForm form) {
		System.out.println("登録しました="+form);
	}
	
	
	
//	@PostMapping("/login")
//	public void login(@RequestParam String loginId, @RequestParam String password) {
//		
//		if (loginId.equals("xxx")) {
//			throw new LoginError("invalid id of password.");
//		}
//		
//	}
//	
//	class LoginError extends RuntimeException {
//		public LoginError(String msg) {
//			super(msg);
//		}
//	}
//	
//	@ExceptionHandler(LoginError.class)
//	@ResponseStatus(HttpStatus.UNAUTHORIZED)
//	public void loginError() {
//		
//	}
}
