package collectionsManager.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import collectionsManager.model.Collection;
import collectionsManager.model.Commentary;
import collectionsManager.model.Item;
import collectionsManager.repository.CollectionRepository;
import collectionsManager.repository.CollectionSearch;
import collectionsManager.repository.CommentaryRepository;
import collectionsManager.repository.CommentarySearch;
import collectionsManager.repository.ItemRepository;
import collectionsManager.repository.ItemSearch;

@Service
public class ItemService {

	@Autowired
	ItemRepository itemRepository;
	@Autowired
	CollectionRepository collectionRepository;
	@Autowired
	private ItemSearch itemSearch;
	@Autowired
	private CollectionSearch collectionSearch;
	@Autowired
	private CommentarySearch commentarySearch;
	@Autowired
	private CommentaryService commentaryService;
	@Autowired
	private CommentaryRepository commentaryRepository;

	public void save(Item item) {
		itemRepository.save(item);
	}

	public Item createNewItem(String itemName, String tags, Long collectionId) {
		Item item = new Item();
		item.setName(itemName);
		item.setCollectionId(collectionId);
		String[] arr = tags.substring(1, tags.length() - 1).split(",");
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i].substring(10, arr[i].length() - 2);
		}
		item.setTags(Arrays.toString(arr));
		itemRepository.save(item);
		return item;
	}

	public Set<Item> findAllItemsInCollection(Collection collection) {
		Set<Item> items = new HashSet<>();
		for (Item it : itemRepository.findAll()) {
			if (it.getCollectionId() == collection.getId()) {
				items.add(it);
			}
		}
		return items;
	}

	public void deleteById(long id) {
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
		itemRepository.delete(item);
		for (Commentary c : commentaryService.findAllForItem(id)) {
			commentaryRepository.delete(c);
		}
		itemRepository.delete(item);
	}

	public Iterable<Item> findAll() {
		return itemRepository.findAll();
	}

	public Item findById(long id) {
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));

		return item;
	}

	public Iterable<Item> findFiveLast() {
		HashSet<Item> lastFive = new HashSet<>();
		ArrayList<Item> items = (ArrayList<Item>) findAll();
		int count = 0;
		Item last = items.get(0);
		if (items.size() > 5) {
			while (count < 5) {
				for (Item d : items) {
					if (!lastFive.contains(d)) {
						last = d;
						break;
					}
				}
				for (Item d : items) {
					if (d.getCreated().after(last.getCreated()) && !lastFive.contains(d)) {
						last = d;
					}
				}
				lastFive.add(last);
				count++;
			}
			return lastFive;
		} else {
			return items;
		}
	}

	public String findAllTags() {
		String tags = "";
		StringBuilder sb = new StringBuilder(tags);
		for (Item it : itemRepository.findAll()) {
			String tag = it.getTags();
			String withoutBrackets = tag.substring(1, tag.length() - 1);
			sb.append(withoutBrackets + ", ");
		}
		return sb.toString();
	}

	public List<Item> searchResults(String q) {
		List<Item> searchResultsItem = null;
		List<Collection> searchResultsCollection = null;
		List<Commentary> searchResultsCommentary = null;
		try {
			searchResultsItem = itemSearch.search(q);
			searchResultsCollection = collectionSearch.search(q);
			searchResultsCommentary = commentarySearch.search(q);
		} catch (Exception ex) {
			System.out.println("An error occurred trying to build the search index: " + ex.toString());
		}
		if (searchResultsItem.isEmpty()) {
			searchResultsItem = new ArrayList<Item>();
		}
		if (!searchResultsCollection.isEmpty()) {
			for (Collection c : searchResultsCollection) {
				Item item = findAllItemsInCollection(c).stream().findFirst()
						.orElseThrow(() -> new IllegalArgumentException("No items in collection"));
				searchResultsItem.add(item);
			}
		}
		if (!searchResultsCommentary.isEmpty()) {
			for (Commentary c : searchResultsCommentary) {
				Item item = findById(c.getItemId());
				searchResultsItem.add(item);
			}
		}
		return searchResultsItem;
	}

	public Item saveItem(String itemName, String tags, long itemId) {
		Item it = findById(itemId);
		it.setName(itemName);
		String[] arr = tags.substring(1, tags.length() - 1).split(",");
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i].substring(10, arr[i].length() - 2);
		}
		it.setTags(Arrays.toString(arr));
		itemRepository.save(it);
		return it;
	}

}
