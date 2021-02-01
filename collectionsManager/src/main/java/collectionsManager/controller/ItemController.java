package collectionsManager.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import collectionsManager.model.Collection;
import collectionsManager.model.Item;
import collectionsManager.model.User;
import collectionsManager.service.CollectionService;
import collectionsManager.service.ItemService;
import collectionsManager.service.MyUserDetailsService;
import collectionsManager.utils.HeaderChooser;

@Controller
public class ItemController {

	@Autowired
	ItemService itemService;
	@Autowired
	CollectionService collectionService;
	@Autowired
	MyUserDetailsService userService;

	@GetMapping(value = "/get-item-description")
	ResponseEntity<Item> getItem(@RequestParam Long itemNumber) {
		return ResponseEntity.status(HttpStatus.OK).body(itemService.findById(itemNumber));
	}

	@GetMapping(value = "/createItem")
	public String createNewItem(@RequestParam("itemName") String itemName, @RequestParam("tags") String tags,
			@RequestParam("collectId") String collectionId, Model model) {
		Item item = itemService.createNewItem(itemName, tags, Long.parseLong(collectionId));
		model.addAttribute("item", item);
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "create";
	}

	@GetMapping(value = "/createItems")
	public String loadCreatePage(@RequestParam("collectionId") String collectionId, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ArrayList<String> userRoles = new ArrayList<>();
		for (GrantedAuthority role : auth.getAuthorities()) {
			userRoles.add(role.getAuthority());
		}
		Collection c = collectionService.findById(Long.parseLong(collectionId));
		User collectionUser = userService.findById(c.getUserId());
		model.addAttribute("header", HeaderChooser.chooseHeader());
		if (!auth.getName().equals(collectionUser.getUsername()) && !userRoles.contains("ROLE_ADMIN")) {
			model.addAttribute("message", "It's not your collection, you can't create items here!!!");
			model.addAttribute("collection", c);
			model.addAttribute("items", itemService.findAllItemsInCollection(c));
			return "collectionPage";
		} else {
			model.addAttribute("collectedId", collectionId);
			model.addAttribute("tag", itemService.findAllTags());
			return "createItems";
		}
	}

	@GetMapping("/edit/{idCollection}/editItem/{id}")
	public String showUpdateFormForItem(@PathVariable("idCollection") long collectionId, @PathVariable("id") long id,
			Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ArrayList<String> userRoles = new ArrayList<>();
		for (GrantedAuthority role : auth.getAuthorities()) {
			userRoles.add(role.getAuthority());
		}
		Collection c = collectionService.findById(collectionId);
		User collectionUser = userService.findById(c.getUserId());
		model.addAttribute("header", HeaderChooser.chooseHeader());
		if (!auth.getName().equals(collectionUser.getUsername()) && !userRoles.contains("ROLE_ADMIN")) {
			model.addAttribute("message", "It's not your collection, you can't edit items here!!!");
			model.addAttribute("collection", c);
			model.addAttribute("items", itemService.findAllItemsInCollection(c));
			return "collectionPage";
		} else {
			model.addAttribute("it", itemService.findById(id));
			return "editItems";
		}
	}

	@GetMapping(value = "/saveChangesInItem")
	public String saveChangedItem(@RequestParam("itemName") String itemName, @RequestParam("tags") String tags,
			@RequestParam("itemId") String itemId, Model model) {
		Item item = itemService.saveItem(itemName, tags, Long.parseLong(itemId));
		model.addAttribute("item", item);
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "create";
	}

	@GetMapping("/edit/{idCollection}/showItem/{id}")
	public String showItem(@PathVariable("id") long id, Model model) {
		model.addAttribute("itId", id);
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "live-commentary";
	}

	@GetMapping("/edit/{idCollection}/deleteItem/{id}")
	public String deleteItem(@PathVariable("id") long id, @PathVariable("idCollection") long collectionId,
			Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ArrayList<String> userRoles = new ArrayList<>();
		for (GrantedAuthority role : auth.getAuthorities()) {
			userRoles.add(role.getAuthority());
		}
		Collection c = collectionService.findById(collectionId);
		User collectionUser = userService.findById(c.getUserId());
		if (!auth.getName().equals(collectionUser.getUsername()) && !userRoles.contains("ROLE_ADMIN")) {
			model.addAttribute("message", "It's not your collection, you can't delete items here!!!");
		} else {
			itemService.deleteById(id);
		}
		model.addAttribute("header", HeaderChooser.chooseHeader());
		model.addAttribute("collection", collectionService.findById(collectionId));
		model.addAttribute("items", itemService.findAllItemsInCollection(collectionService.findById(collectionId)));
		return "collectionPage";
	}

	@GetMapping("/searchItem")
	public String search(@RequestParam String tags, Model model) {
		model.addAttribute("searchResults", itemService.searchResults(tags));
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "search";
	}
}
