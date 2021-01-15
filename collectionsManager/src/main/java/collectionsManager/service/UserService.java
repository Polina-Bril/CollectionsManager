package collectionsManager.service;

import collectionsManager.model.User;

public interface UserService {

	User findById(int id);

	void block(User user);

	void delete(User user);

	User grantAdminRole(User user);

}
