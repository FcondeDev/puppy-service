package com.puppy.exception;

import com.puppy.dto.ErrorDto;
import lombok.Getter;

@Getter
public class ImageStorageException extends RuntimeException {

  private final ErrorDto errorDto;

  public ImageStorageException(ErrorDto errorDto, Throwable cause) {
    super(errorDto.getDescription(), cause);
    this.errorDto = errorDto;
  }

  public ImageStorageException(ErrorDto errorDto) {
    super(errorDto.getDescription());
    this.errorDto = errorDto;
  }
}
