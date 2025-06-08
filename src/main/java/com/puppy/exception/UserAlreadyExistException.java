package com.puppy.exception;

import com.puppy.dto.ErrorDto;
import lombok.Getter;

@Getter
public class UserAlreadyExistException extends RuntimeException {

  private final ErrorDto errorDto;

  public UserAlreadyExistException(ErrorDto errorDto) {
    super(errorDto.getDescription());
    this.errorDto = errorDto;
  }
}
