package collectionsManager.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	
	private int id;
	private String text;
	private User user;
	private Date created;
	private int itemId;

}
