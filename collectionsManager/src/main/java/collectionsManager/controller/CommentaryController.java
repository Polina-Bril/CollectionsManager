package collectionsManager.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import collectionsManager.model.Comment;
import collectionsManager.model.Commentary;
import collectionsManager.service.CommentaryService;
import collectionsManager.utils.MarkdownConverter;

@Controller
public class CommentaryController {
	@Autowired
	CommentaryService commentaryService;

	@MessageMapping("/live-comment")
	@SendTo("/topic/commentary")
	public Comment liveComment(Commentary message) throws Exception {
		System.out.println(message);
		commentaryService.save(message);
		return new Comment(HtmlUtils.htmlEscape(message.getCommentary()));
	}

	@GetMapping(value = "/all-comments")
	ResponseEntity<Iterable<Commentary>> getAll(@RequestParam Long itemNumber) {
		Set<Commentary> comments = commentaryService.findAllForItem(itemNumber);
		for (Commentary c : comments) {
			c.setCommentary(MarkdownConverter.markdownToHTML(c.getCommentary()));
		}
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}
}