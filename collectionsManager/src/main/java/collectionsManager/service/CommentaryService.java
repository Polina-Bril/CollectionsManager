package collectionsManager.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import collectionsManager.model.Commentary;
import collectionsManager.repository.CommentaryRepository;
import collectionsManager.repository.ItemRepository;

@Service
public class CommentaryService {
	@Autowired
	CommentaryRepository commentaryRepository;
	@Autowired
	ItemRepository itemRepository;
	
	
	public void save(Commentary commentary) {
		commentaryRepository.save(commentary);
		}

	public Iterable<Commentary> findAll() {
		return commentaryRepository.findAll();

	}

	public Set<Commentary> findAllForItem(Long itemNumber) {
		Set<Commentary> comments = new HashSet<>();
		for (Commentary c : commentaryRepository.findAll()) {
			if (c.getItemId() == itemNumber) {
				comments.add(c);
			}
		}		return comments;
	}

}
