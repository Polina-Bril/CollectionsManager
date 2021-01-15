package collectionsManager.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	
	private int id;
	private String name;
	private List<String> tags;
	private List<Comment> comments;
	private List<User> likes;
	private User user;
	private int collectionId;
	private int price;
	private int strenth;
	private int pages;
	private String author;
	private String countryFrom;
	private String genre;
	private String about;
	private String creationHistory;
	private String acquiringHistory;
	private Date created;
	private Date acquired;
	private Date expired;
	
}
