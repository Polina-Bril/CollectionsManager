package collectionsManager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String username;
	private String password;
	private String email;
	private boolean active;
	private String roles;

	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.active = true;
		this.roles = "ROLE_USER";
	}

}
