package com.puppy.service;

import com.puppy.dto.ErrorDto;
import com.puppy.exception.ImageNotFoundException;
import com.puppy.exception.ImageStorageException;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class LocalStorageService implements StorageService {

  private static final String UNABLE_TO_PROCESS_IMAGE = "UNABLE_TO_PROCESS_IMAGE";
  private static final String IMAGE_NOT_FOUND = "IMAGE_NOT_FOUND";

  @Value("${app.images.path}")
  private String imagesPath;

  @PostConstruct
  void init() throws IOException {
    log.info("Removing files from File Storage");
    FileUtils.cleanDirectory(new File(imagesPath));
  }

  @Override
  public void storeImage(MultipartFile file, String imageName) {
    try {
      log.info("Storing image:{}", imageName);
      file.transferTo(new File(imagesPath + imageName));
    } catch (Exception e) {
      throw new ImageStorageException(
          new ErrorDto(UNABLE_TO_PROCESS_IMAGE, "Unable of processing Image"), e.getCause());
    }
  }

  @Override
  public Resource getImage(String imageName) {

    Path path = Paths.get(imagesPath + imageName);

    if (!Files.exists(path)) {
      throw new ImageNotFoundException(new ErrorDto(IMAGE_NOT_FOUND, "Image does not exists"));
    }

    try {
      log.info("Getting image:{}", imageName);
      return new UrlResource(path.toUri());
    } catch (Exception e) {
      throw new ImageStorageException(
          new ErrorDto(UNABLE_TO_PROCESS_IMAGE, "Unable of processing Image"), e.getCause());
    }
  }
}
