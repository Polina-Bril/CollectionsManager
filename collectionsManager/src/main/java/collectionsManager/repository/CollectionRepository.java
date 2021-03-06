package collectionsManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import collectionsManager.model.Collection;

@Repository
public interface CollectionRepository extends CrudRepository<Collection, Long>{

}
