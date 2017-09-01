package jsug.portside.api.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import jsug.portside.api.entity.Speaker;

public interface SpeakerRepository extends PagingAndSortingRepository<Speaker, UUID> {

	
	
}
