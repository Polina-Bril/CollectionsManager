package collectionsManager.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Indexed
@Table(name = "item")
@Setter
@Getter
@EqualsAndHashCode(of = {"id", "name", "tags"})
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "item_id", nullable = false)
	private Long id;
	
	@Column(name = "item_name")
	@Field
	private String name;
	
	@Column(name = "tags")
	@Field
	private String tags;
	private Long[] likesUserIds;
	private Long collectionId;

//	private int price;//00000000 00000001
//	private int strenth;//00000000 00000010
//	private int pages;//00000000 00000100
//	@Field
//	private String author;////00000000 00001000
//	@Field
//	private String countryFrom;//00000000 00010000
//	@Field
//	private String genre;//00000000 00100000
//	@Field
//	private String about;//00000000 01000000
//	@Field
//	private String creationHistory;//00000000 10000000
//	@Field
//	private String acquiringHistory;//00000001 00000000
	private Date created = new Date();//00000010 00000000
//	private Date acquired;//00000100 00000000
//	private Date expired;//00001000 00000000
//	private boolean isFavorite;//00010000 00000000
//	private boolean isReadyToExchange;//00100000 00000000
//	private boolean isRead;//01000000 00000000
//	private byte bitMask = 0000000000000000;
}
