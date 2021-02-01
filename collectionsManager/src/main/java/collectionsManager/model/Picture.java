package collectionsManager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "cloud_images")
@Data
public class Picture {

	@Id
	@GeneratedValue
	private Long id;

	private String publicUri;

	@Override
	public String toString() {
		return "Picture{" + "id=" + id + ", publicUri='" + publicUri + '}';
	}
}