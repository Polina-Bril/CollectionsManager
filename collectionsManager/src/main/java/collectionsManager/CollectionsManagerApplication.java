package collectionsManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import collectionsManager.repository.UserRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class CollectionsManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectionsManagerApplication.class, args);
	}

}
