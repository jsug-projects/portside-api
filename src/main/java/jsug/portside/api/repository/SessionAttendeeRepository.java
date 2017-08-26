package jsug.portside.api.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import jsug.portside.api.entity.SessionAttendee;

public interface SessionAttendeeRepository extends PagingAndSortingRepository<SessionAttendee, UUID>{

}
