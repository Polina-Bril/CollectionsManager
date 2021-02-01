package collectionsManager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import collectionsManager.utils.ThemaConverter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Indexed
@Table(name = "collection")
@Setter
@Getter
@EqualsAndHashCode(of = { "id", "name", "thema" })
@NoArgsConstructor
@AllArgsConstructor
public class Collection {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "collection_id", nullable = false)
	private Long id;

	@Column(name = "collection_name")
	@Field
	private String name;
	private Long userId;
	@Enumerated(EnumType.STRING)
	private Thema thema;
	private String pictureURL;
	@Field
	private String about;

	public Collection(String collectionName, String about, String thema) {
		this.name = collectionName;
		this.about = about;
		ThemaConverter themaConverter = new ThemaConverter();
		this.thema = themaConverter.convertToEntityAttribute(thema);
	}

	public Collection(String collectionName, String about, String thema, Long userId) {
		this.name = collectionName;
		this.about = about;
		ThemaConverter themaConverter = new ThemaConverter();
		this.thema = themaConverter.convertToEntityAttribute(thema);
		this.userId = userId;
	}
}
