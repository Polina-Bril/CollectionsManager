package collectionsManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import collectionsManager.model.Collection;
import collectionsManager.service.CollectionService;
import collectionsManager.service.ItemService;
import collectionsManager.service.MyUserDetailsService;
import collectionsManager.service.StorageService;
import collectionsManager.utils.HeaderChooser;
import collectionsManager.utils.MarkdownConverter;

@Controller
public class MainController {

	@Autowired
	private ItemService itemService;
	@Autowired
	private CollectionService collectionService;
	@Autowired
	private MyUserDetailsService userService;
	@Autowired
	StorageService storageService;

	@GetMapping(value = "/")
	public String findFiveLastItems(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		if (!username.equals("anonymousUser")) {
			String authMessage = "Hello, " + username;
			model.addAttribute("loginMessage", authMessage);
		}
		model.addAttribute("header", HeaderChooser.chooseHeader());
		model.addAttribute("items", itemService.findFiveLast());
		Iterable<Collection> collections = collectionService.findBiggest();
		for (Collection c : collections) {
			c.setAbout(MarkdownConverter.markdownToHTML(c.getAbout()));
		}
		model.addAttribute("collection", collections);
		model.addAttribute("tag", itemService.findAllTags());
		return "index";
	}

	@GetMapping(value = "/registry")
	public String loadRegistryPage(Model model) {
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "createUsers";
	}

	@GetMapping("/search")
	public String search(@RequestParam String q, Model model) {
		model.addAttribute("header", HeaderChooser.chooseHeader());
		model.addAttribute("parametr", q);
		model.addAttribute("searchResults", itemService.searchResults(q));
		return "search";
	}

	@GetMapping(value = "/createUser")
	public String registerNewUser(@RequestParam("username") String userName, @RequestParam("password") String password,
			@RequestParam("email") String email, Model model) {
		String message = userService.createNewUser(userName, password, email);
		model.addAttribute("header", HeaderChooser.chooseHeader());
		if (message.equals("")) {
			String succsessMessage = "Registration Done Succsessfully! Hello, " + userName + ". Please, login now.";
			model.addAttribute("registrationSuccess", succsessMessage);
			model.addAttribute("items", itemService.findFiveLast());
			model.addAttribute("collection", collectionService.findBiggest());
			model.addAttribute("tag", itemService.findAllTags());
			model.addAttribute("header", "header2.html");
			return "index";
		} else {
			model.addAttribute("message", message);
			return "createUsers";
		}
	}
}