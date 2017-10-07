package jsug.portside.api.dto;

public class MailData {
	public String subject;
	public String body;
	public String mailTo;

	public MailData(String subject, String body, String mailTo) {
		this.subject = subject;
		this.body = body;
		this.mailTo = mailTo;
	}

	public MailData() {
	}

	
	

	@Override
	public String toString() {
		return "Hello{" +
				"subject='" + subject + '\'' +
				'}';
	}
}