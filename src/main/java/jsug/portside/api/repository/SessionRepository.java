package jsug.portside.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import jsug.portside.api.entity.Session;

public interface SessionRepository extends PagingAndSortingRepository<Session, UUID>, SessionRepositoryCustom {

	//TODO:Group byでハマりそうなので、いったん、泥臭い方法で取得する
//	@Query("select s, count(s.sessionAttendees) from Session s "
//			+ "left join s.sessionAttendees "
//			+ "group by s")
//	Object[] findSessionsWithAttendeeCount();

}
