package com.puppy.exception;

import com.puppy.dto.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class ExceptionHandlerAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UserNotFoundException.class)
  public ErrorDto handleUserNotFoundException(UserNotFoundException ex) {
    log.error(ex.getErrorDto().getDescription());
    return ex.getErrorDto();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserAlreadyExistException.class)
  public ErrorDto handleUserAlreadyExistException(UserAlreadyExistException ex) {
    log.error(ex.getErrorDto().getDescription());
    return ex.getErrorDto();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ImageStorageException.class)
  public ErrorDto handleImageStorageException(ImageStorageException ex) {
    log.error(ex.getErrorDto().getDescription(), ex.getCause());
    return ex.getErrorDto();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ImageNotFoundException.class)
  public ErrorDto handleImageNotFoundException(ImageNotFoundException ex) {
    log.error(ex.getErrorDto().getDescription(), ex.getCause());
    return ex.getErrorDto();
  }
}
