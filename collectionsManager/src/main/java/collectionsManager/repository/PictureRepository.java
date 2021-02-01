package collectionsManager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import collectionsManager.model.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

	Optional<Picture> findByPublicUri(String publicUri);
}