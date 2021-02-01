package collectionsManager.controller;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import collectionsManager.model.Collection;
import collectionsManager.model.User;
import collectionsManager.service.CollectionService;
import collectionsManager.service.ItemService;
import collectionsManager.service.MyUserDetailsService;
import collectionsManager.service.StorageService;
import collectionsManager.utils.HeaderChooser;
import collectionsManager.utils.MarkdownConverter;

@Controller
public class CollectionController {

	@Autowired
	CollectionService collectionService;
	@Autowired
	ItemService itemService;
	@Autowired
	MyUserDetailsService userService;
	@Autowired
	StorageService storageService;

	@GetMapping(value = "/createCollection")
	public String createNewCollection(@RequestParam("userId") String userId,
			@RequestParam("collectionName") String collectionName, @RequestParam("about") String about,
			@RequestParam("dropThema") String thema, Model model) {
		User user = userService.findById(Long.parseLong(userId));
		Collection c = collectionService.createNewCollection(collectionName, about, thema, user.getUsername());
		c.setAbout(MarkdownConverter.markdownToHTML(about));
		model.addAttribute("collection", c);
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "createColl";
	}

	@GetMapping(value = "/createCollections")
	public String loadCreatePage(@RequestParam("userId") String userId, Model model) {
		model.addAttribute("userId", userId);
		model.addAttribute("themas", collectionService.getAllThemas());
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "createCollections";
	}

	@GetMapping(value = "/userCollections")
	public String showAllCollections(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Set<Collection> collections = collectionService.findAllByUser(username);
		for (Collection c : collections) {
			c.setAbout(MarkdownConverter.markdownToHTML(c.getAbout()));
		}
		model.addAttribute("header", HeaderChooser.chooseHeader());
		model.addAttribute("userId", userService.findByUserName(username).getId());
		model.addAttribute("collections", collections);
		return "collections";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateFormForCollection(@PathVariable("id") long id, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ArrayList<String> userRoles = new ArrayList<>();
		for (GrantedAuthority role : auth.getAuthorities()) {
			userRoles.add(role.getAuthority());
		}
		Collection c = collectionService.findById(id);
		c.setAbout(MarkdownConverter.markdownToHTML(c.getAbout()));
		User collectionUser = userService.findById(c.getUserId());
		model.addAttribute("header", HeaderChooser.chooseHeader());
		if (!auth.getName().equals(collectionUser.getUsername()) && !userRoles.contains("ROLE_ADMIN")) {
			model.addAttribute("message", "It's not your collection, you can't edit it!!!");
			model.addAttribute("collection", c);
			model.addAttribute("items", itemService.findAllItemsInCollection(c));
			return "collectionPage";
		} else {
			model.addAttribute("c", c);
			model.addAttribute("themas", collectionService.getAllThemas());
			return "editCollections";
		}
	}

	@GetMapping(value = "/saveChangesInCollection")
	public String saveChangesInCollection(@RequestParam("collectionId") Long collectionId,
			@RequestParam("collectionName") String collectionName, @RequestParam("about") String about,
			@RequestParam("dropThema") String thema, Model model) {
		Collection c = collectionService.saveCollection(collectionId, collectionName, about, thema);
		c.setAbout(MarkdownConverter.markdownToHTML(c.getAbout()));
		model.addAttribute("collection", c);
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "createColl";
	}

	@GetMapping("/show/{id}")
	public String showCollection(@PathVariable("id") long id, Model model) {
		Collection c = collectionService.findById(id);
		c.setAbout(MarkdownConverter.markdownToHTML(c.getAbout()));
		model.addAttribute("collection", c);
		model.addAttribute("items", itemService.findAllItemsInCollection(collectionService.findById(id)));
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "collectionPage";
	}

	@GetMapping("/delete/{id}")
	public String deleteCollection(@PathVariable("id") long id, Model model) {
		Long userId = collectionService.findById(id).getUserId();
		collectionService.deleteById(id);
		Set<Collection> collections = collectionService.findAllByUser(userService.findById(userId).getUsername());
		for (Collection c : collections) {
			c.setAbout(MarkdownConverter.markdownToHTML(c.getAbout()));
		}
		model.addAttribute("userId", userId);
		model.addAttribute("collections", collections);
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "collections";
	}
}
