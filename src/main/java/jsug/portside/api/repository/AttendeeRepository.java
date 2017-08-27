package jsug.portside.api.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import jsug.portside.api.entity.Attendee;

public interface AttendeeRepository extends PagingAndSortingRepository<Attendee, UUID>{

	Attendee findByEmail(String email);

}
