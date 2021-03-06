package jsug.portside.api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Speaker {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(columnDefinition = "binary(16)")	
	public UUID id;

	@NotEmpty
	public String name;
	public String belongTo;
	public String profile;
	public String imageUrl;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@JsonIgnore
	public byte[] image;
	
	@JsonIgnore
	@ManyToMany(mappedBy="speakers")
	public List<Session> sessions = new ArrayList<>();
	
	public void updateId(UUID id) {
		this.id = id;
	}


	public void updateImage(byte[] bytes) {
		this.image = bytes;
	}
	
	public void updateImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
