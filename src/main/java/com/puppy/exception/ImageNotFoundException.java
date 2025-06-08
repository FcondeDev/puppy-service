package com.puppy.exception;

import com.puppy.dto.ErrorDto;
import lombok.Getter;

@Getter
public class ImageNotFoundException extends RuntimeException {

  private final ErrorDto errorDto;

  public ImageNotFoundException(ErrorDto errorDto, Throwable cause) {
    super(errorDto.getDescription(), cause);
    this.errorDto = errorDto;
  }

  public ImageNotFoundException(ErrorDto errorDto) {
    super(errorDto.getDescription());
    this.errorDto = errorDto;
  }
}
