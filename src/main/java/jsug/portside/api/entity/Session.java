package jsug.portside.api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Session {
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(columnDefinition = "binary(16)")	
	public UUID id;
	
	@NotEmpty
	public String title;
	public String description;
	public String speaker;

	@ManyToMany
	@JoinTable(
      name="session_attendee",
      joinColumns=@JoinColumn(name="session_id", referencedColumnName="id"),
      inverseJoinColumns=@JoinColumn(name="attendee_id", referencedColumnName="id"))
	@JsonIgnore
	public List<Attendee> attendees = new ArrayList<>();
	

	public void updateId(UUID id) {
		this.id = id;
	}	
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void attended(Attendee attendee) {
		this.attendees.add(attendee);		
	}

	public void unAttended(Attendee attendee) {
		int idx = -1;
		for (int i=0; i<attendees.size(); i++) {
			if (attendee.id.equals(attendees.get(i).id)) {
				idx = i;
			}
		}
		if (idx != -1) {
			attendees.remove(idx);
		}
	}
}
