package collectionsManager.controller;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import collectionsManager.service.StorageService;

@RestController
public class UploadController {
	@Autowired
    StorageService storageService;

    @PostMapping(value = "/api/upload/multi/{id}")
    public ResponseEntity<?> uploadFileToCloudinary(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) {
        if(StringUtils.isEmpty(file.getOriginalFilename()) || file.isEmpty()) {
            return new ResponseEntity("uploaded file should not be empty", HttpStatus.BAD_REQUEST);
        }
        try {
            storageService.store(file, Long.parseLong(id));
        } catch (IOException e) {
            return new ResponseEntity<>("file upload exception", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.OK)
              .body("Status ok");
    }
}