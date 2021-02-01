package collectionsManager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import collectionsManager.model.Collection;
import collectionsManager.model.MyUserDetails;
import collectionsManager.model.User;
import collectionsManager.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	CollectionService collectionService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
		return user.map(MyUserDetails::new).get();
	}

	public String createNewUser(String username, String password, String email) {
		String message = "";
		for (User u : userRepository.findAll()) {
			if (u.getEmail().equals(email)) {
				message = "User with such email is already exist!";
				break;
			} else if (u.getUsername().equals(username)) {
				message = "Chose another username! " + username + " is already exist.";
				break;
			}
		}
		if (message.equals("")) {
			String encryptedPassword = passwordEncoder.encode(password);
			userRepository.save(new User(username, encryptedPassword, email));
		}
		return message;
	}

	public User findById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid userId " + userId));
	}

	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	public User saveUser(long id, String userName, String password, String email, Boolean active, String roles) {
		String encryptedPassword = passwordEncoder.encode(password);
		User user = new User(id, userName, encryptedPassword, email, active, roles);
		userRepository.save(user);
		return user;
	}

	public void deleteById(long id) {
		User user = findById(id);
		userRepository.delete(user);
		for (Collection c : collectionService.findAllByUser(user.getId())) {
			collectionService.deleteById(c.getId());
		}
	}

	public User findByUserName(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Invalid username " + username));
	}
}
