package collectionsManager.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collection {

	private int id;
	private String name;
	private User user;
	private String thema;
	private String pictureURL;
	private String about;
	private List<Item>items;
	
}
