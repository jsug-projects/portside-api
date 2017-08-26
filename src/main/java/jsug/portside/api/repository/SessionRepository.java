package jsug.portside.api.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import jsug.portside.api.entity.Session;

@Repository
public interface SessionRepository extends PagingAndSortingRepository<Session, UUID>{



}
