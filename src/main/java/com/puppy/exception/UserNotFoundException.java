package com.puppy.exception;

import com.puppy.dto.ErrorDto;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

  private final ErrorDto errorDto;

  public UserNotFoundException(ErrorDto errorDto) {
    super(errorDto.getDescription());
    this.errorDto = errorDto;
  }
}
