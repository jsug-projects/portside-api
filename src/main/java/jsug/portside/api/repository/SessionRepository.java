package jsug.portside.api.repository;

import java.util.List;
import java.util.UUID;

import jsug.portside.api.entity.Speaker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import jsug.portside.api.entity.Session;

public interface SessionRepository extends PagingAndSortingRepository<Session, UUID>, SessionRepositoryCustom {


	@Query("select session from Session session where session.id in ?1")
	Iterable<Session> findAll(List<UUID> ids);


	List<Session> findByAttendeesId(UUID attendeeId);

	//TODO:Group byでハマりそうなので、いったん、泥臭い方法で取得する
//	@Query("select s, count(s.sessionAttendees) from Session s "
//			+ "left join s.sessionAttendees "
//			+ "group by s")
//	Object[] findSessionsWithAttendeeCount();

}
