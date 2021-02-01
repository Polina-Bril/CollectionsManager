package collectionsManager.utils;

import java.util.ArrayList;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class HeaderChooser {

	public static String chooseHeader() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ArrayList<String> userRoles = new ArrayList<>();
		for (GrantedAuthority role : auth.getAuthorities()) {
			userRoles.add(role.getAuthority());
		}
		String username = auth.getName();
		if (!username.equals("anonymousUser")) {
			if (userRoles.contains("ROLE_ADMIN")) {
				return "header3.html";
			} else {
				return "header.html";
			}
		} else {
			return "header2.html";
		}
	}
}
