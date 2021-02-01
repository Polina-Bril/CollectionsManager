package collectionsManager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Indexed
@Setter
@ToString
@Getter
@EqualsAndHashCode(of = { "id", "commentary" })
@NoArgsConstructor
@AllArgsConstructor
public class Commentary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Field
	private String commentary;
	private Long itemId;

	public Commentary(String commentary, String itemId) {
		super();
		this.commentary = commentary;
		this.itemId = Long.parseLong(itemId);
	}
}