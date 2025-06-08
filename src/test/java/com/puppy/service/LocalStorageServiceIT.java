package com.puppy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.puppy.AbstractIT;
import com.puppy.exception.ImageNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

class LocalStorageServiceIT extends AbstractIT {

  private static final String IMAGES_NAME = "01a2b3c-53243-4def-9012-34544589abcddf0.png";

  @Value("${app.images.path}")
  private String imagesPath;

  @Autowired private LocalStorageService localStorageService;

  @Test
  void store_givenImage_newImageIsStoreLocally() throws IOException {
    // given
    File image = ResourceUtils.getFile("classpath:images/test.png");
    MockMultipartFile file =
        new MockMultipartFile(image.getName(), Files.readAllBytes(image.toPath()));
    // when
    localStorageService.storeImage(file, IMAGES_NAME);
    // then
    File newFile = new File(imagesPath + IMAGES_NAME);

    assertThat(newFile.exists()).isTrue();
  }

  @Test
  void getImage_imageDoesNotExists_ExceptionIsThrown() {
    // given + when
    ImageNotFoundException response =
        assertThrows(
            ImageNotFoundException.class, () -> localStorageService.getImage(IMAGES_NAME + "fake"));

    // then
    assertThat(response.getMessage()).isEqualTo("Image does not exists");
  }
}
