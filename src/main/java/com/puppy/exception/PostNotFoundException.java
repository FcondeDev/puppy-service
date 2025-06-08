package com.puppy.exception;

import com.puppy.dto.ErrorDto;
import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException {

  private final ErrorDto errorDto;

  public PostNotFoundException(ErrorDto errorDto) {
    super(errorDto.getDescription());
    this.errorDto = errorDto;
  }
}
