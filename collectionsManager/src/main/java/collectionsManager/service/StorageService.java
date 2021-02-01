package collectionsManager.service;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import collectionsManager.model.Collection;
import collectionsManager.model.Picture;
import collectionsManager.repository.PictureRepository;

@Service
public class StorageService {

	private final Cloudinary cloudinary;
	private final PictureRepository pictureRepository;
	private final CollectionService collectionService;

	@Autowired
	public StorageService(PictureRepository repository, CollectionService collectionService) {
		this.pictureRepository = repository;
		this.collectionService = collectionService;
		this.cloudinary = initCloudinary();
	}

	public void store(MultipartFile file, long collectionId) throws IOException {
		storeToCloudinary(file, collectionId);
	}

	private void storeToCloudinary(MultipartFile file, long collectionId) throws IOException {
		if (cloudinary == null) {
			initCloudinary();
		}
		File fileToUpLoad = new File(file.getOriginalFilename());
		FileUtils.writeByteArrayToFile(fileToUpLoad, file.getBytes());

		cloudinary.uploader().upload(fileToUpLoad, ObjectUtils.asMap("public_id", file.getOriginalFilename()));

		String generatedUri = cloudinary.url().generate(file.getOriginalFilename() + ".jpg");
		Picture p = new Picture();
		p.setPublicUri(generatedUri);
		Collection c = collectionService.findById(collectionId);
		c.setPictureURL(generatedUri);
		pictureRepository.save(p);
	}

	public Stream<Picture> loadAll() {
		return pictureRepository.findAll().stream();
	}

	private Cloudinary initCloudinary() {
		return new Cloudinary(ObjectUtils.asMap("cloud_name", "poli1najdk", "api_key", "226884845594895", "api_secret",
				"jH3OioKwuZrDj4nsom0NuGFhZyA"));
	}
}