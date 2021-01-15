package collectionsManager.service;

import java.util.List;

import collectionsManager.model.Collection;
import collectionsManager.model.Comment;
import collectionsManager.model.Item;
import collectionsManager.model.User;

public interface ItemService {
	
	List<Item> getAllByCollection(Collection collection);
	List<Item> getFiveLastCreated();
	List<Item> findAllByTag(String tag);
	Item save(Item item);
	Item findById(int id);
	Item update(Item item);
	void delete(Item item);
	List<User>like(Item item);
	List<Comment> getAllCommentsByItem(Item item);
	Item saveComment(Item item, Comment comment);
	
	


}
