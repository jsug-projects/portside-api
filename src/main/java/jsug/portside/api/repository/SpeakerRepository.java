package jsug.portside.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import jsug.portside.api.entity.Speaker;

public interface SpeakerRepository extends PagingAndSortingRepository<Speaker, UUID> {

    @Query("select speaker from Speaker speaker where speaker.id IN ?1")
    Iterable<Speaker> findAll(List<UUID> ids);

}
