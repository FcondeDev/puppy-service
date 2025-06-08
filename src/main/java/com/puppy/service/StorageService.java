package com.puppy.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  void storeImage(MultipartFile file, String imageName);

  Resource getImage(String imageName);
}
