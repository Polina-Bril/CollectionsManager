package collectionsManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import collectionsManager.model.Commentary;

@Repository
public interface CommentaryRepository extends CrudRepository<Commentary, Long> {
	
}
