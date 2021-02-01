package collectionsManager.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import collectionsManager.model.Collection;
import collectionsManager.service.CollectionService;
import collectionsManager.service.MyUserDetailsService;
import collectionsManager.utils.HeaderChooser;
import collectionsManager.utils.MarkdownConverter;

@Controller
public class AdminController {

	@Autowired
	private CollectionService collectionService;
	@Autowired
	private MyUserDetailsService userService;

	@GetMapping(value = "/admin")
	public String findAllUsers(Model model) {
		model.addAttribute("users", userService.findAll());
		return "users";
	}

	@GetMapping("/admin/edit/{id}")
	public String showUpdateFormForUser(@PathVariable("id") long id, Model model) {
		model.addAttribute("user", userService.findById(id));
		return "editUsers";
	}

	@GetMapping(value = "/admin/saveChangesInUser")
	public String saveChangedUser(@RequestParam("username") String userName, @RequestParam("password") String password,
			@RequestParam("email") String email, @RequestParam("userId") String id,
			@RequestParam("active") String active, @RequestParam("roles") String roles, Model model) {
		userService.saveUser(Long.parseLong(id), userName, password, email, new Boolean(active), roles);
		model.addAttribute("users", userService.findAll());
		return "users";
	}

	@GetMapping("/admin/show/{id}")
	public String showUserPage(@PathVariable("id") long id, Model model) {
		Set<Collection> collections = collectionService.findAllByUser(id);
		for (Collection c : collections) {
			c.setAbout(MarkdownConverter.markdownToHTML(c.getAbout()));
		}
		model.addAttribute("collections", collections);
		model.addAttribute("userId", id);
		model.addAttribute("header", HeaderChooser.chooseHeader());
		return "collections";
	}

	@GetMapping("/admin/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		userService.deleteById(id);
		model.addAttribute("users", userService.findAll());
		return "users";
	}
}