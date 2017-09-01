package jsug.portside.api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Lists;

import jsug.portside.api.entity.Speaker;
import jsug.portside.api.repository.SpeakerRepository;

@RestController
@RequestMapping("/speakers")
public class SpeakerController {

	@Autowired
	SpeakerRepository speakerRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> register(@RequestBody @Validated Speaker speaker,
			@Value("#{request.requestURL}") String url) {
		
		speakerRepository.save(speaker);
		
		URI location = ServletUriComponentsBuilder
				.fromUriString(url)
				.path("/{id}")
				.buildAndExpand(speaker.id)
				.toUri();

		return ResponseEntity.created(location).build();
		
		
	}

	@GetMapping
	public List<Speaker> gets() {
		return Lists.newArrayList(speakerRepository.findAll());		
	}
	
	@GetMapping("/{id}")
	public Speaker get(@PathVariable UUID id) {
		return speakerRepository.findOne(id);
	}
	
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)	
	public void update(@RequestBody @Validated Speaker speaker, @PathVariable UUID id) {
		if (speaker.id == null) {
			speaker.updateId(id);
		}
		speakerRepository.save(speaker);	
	}
	
	@PostMapping("/{id}/image")
	@ResponseStatus(HttpStatus.CREATED)	
	@Transactional
	public void registerImage(@PathVariable UUID id, @RequestParam MultipartFile data) throws Exception {
		Speaker speaker = speakerRepository.findOne(id);
		speaker.image = data.getBytes();
	}
	
	@GetMapping("/{id}/image")
	public byte[] getImage(@PathVariable UUID id) {
		Speaker speaker = speakerRepository.findOne(id);
		return speaker.image;
	}
	
	
}
