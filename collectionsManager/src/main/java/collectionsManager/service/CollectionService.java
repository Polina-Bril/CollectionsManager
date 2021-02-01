package collectionsManager.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import collectionsManager.model.Collection;
import collectionsManager.model.Item;
import collectionsManager.model.Thema;
import collectionsManager.model.User;
import collectionsManager.repository.CollectionRepository;
import collectionsManager.repository.UserRepository;
import collectionsManager.utils.ThemaConverter;

@Service
public class CollectionService {

	@Autowired
	CollectionRepository collectionRepository;
	@Autowired
	ItemService itemService;
	@Autowired
	UserRepository userRepository;

	public Collection createNewCollection(String collectionName, String about, String thema, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Invalid username" + username));
		Collection collection = new Collection(collectionName, about, thema, user.getId());
		return collectionRepository.save(collection);
	}

	public Set<String> getAllThemas() {
		Set<String> themas = new HashSet<>();

		for (Thema th : Thema.values()) {
			themas.add(new ThemaConverter().convertToDatabaseColumn(th));
		}
		return themas;
	}

	public Collection findByName(String collectionName) {
		for (Collection c : collectionRepository.findAll()) {
			if (collectionName.equals(c.getName())) {
				return c;
			}
		}
		return null;
	}

	public Iterable<Collection> findAll() {
		return collectionRepository.findAll();
	}

	public Collection findById(long id) {
		Collection collection = collectionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid collection Id:" + id));

		return collection;
	}

	public void deleteById(long id) {
		Collection collection = findById(id);
		collectionRepository.delete(collection);
		for (Item it : itemService.findAllItemsInCollection(collection)) {
			itemService.deleteById(it.getId());
		}
	}

	public Iterable<Collection> findBiggest() {
		HashSet<Collection> biggestFive = new HashSet<>();
		ArrayList<Collection> collections = (ArrayList<Collection>) findAll();
		int count = 0;
		Collection biggest = collections.get(0);
		if (collections.size() > 5) {
			while (count < 5) {
				for (Collection d : collections) {
					if (!biggestFive.contains(d)) {
						biggest = d;
						break;
					}
				}
				for (Collection d : collections) {
					Set<Item> dItems = itemService.findAllItemsInCollection(d);
					Set<Item> biggestItems = itemService.findAllItemsInCollection(biggest);
					if (dItems.size() > biggestItems.size() && !biggestFive.contains(d)) {
						biggest = d;
					}
				}
				biggestFive.add(biggest);
				count++;
			}
			return biggestFive;
		} else {
			return collections;
		}
	}

	public Set<Collection> findAllByUser(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Invalid username" + username));
		HashSet<Collection> userCollection = new HashSet<>();
		ArrayList<Collection> collections = (ArrayList<Collection>) findAll();
		for (Collection d : collections) {
			if (d.getUserId() == user.getId()) {
				userCollection.add(d);
			}
		}
		return userCollection;
	}

	public Collection saveCollection(Long collectionId, String collectionName, String about, String thema) {
		Collection c = findById(collectionId);
		c.setName(collectionName);
		c.setAbout(about);
		c.setThema(new ThemaConverter().convertToEntityAttribute(thema));
		collectionRepository.save(c);
		return c;
	}

	public Set<Collection> findAllByUser(long id) {
		HashSet<Collection> userCollection = new HashSet<>();
		Iterable<Collection> collections = findAll();
		for (Collection d : collections) {
			if (d.getUserId() == id) {
				userCollection.add(d);
			}
		}
		return userCollection;
	}
}
