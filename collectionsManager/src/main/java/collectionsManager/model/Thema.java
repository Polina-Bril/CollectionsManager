package collectionsManager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Thema {
	BOOKS ("books"), STAMPS ("stamps"), PICTURES ("pictures"), WHISKEY ("whiskey"), BADGES("badges");

	@Getter
	private final String thema;
}

