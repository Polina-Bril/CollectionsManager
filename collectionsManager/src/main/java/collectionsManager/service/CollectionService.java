package collectionsManager.service;

import java.util.List;
import java.util.Map;

import collectionsManager.model.Collection;
import collectionsManager.model.User;

public interface CollectionService {

	List<Collection> findByUser(User user);

	Collection findById(int id);
	
	Collection findTheBiggest();

	Collection save(Collection collection, byte[] file);

	Collection update(Collection collection);

	void delete(Collection collection);

//    Picture getPictureById(String url);
	
	Map<String, String> savePicture(byte[] file);
}
