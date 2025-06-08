package com.puppy.service;

import com.puppy.dto.ErrorDto;
import com.puppy.dto.UserDto;
import com.puppy.exception.UserAlreadyExistException;
import com.puppy.mapper.UserMapper;
import com.puppy.model.User;
import com.puppy.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private static final String USER_ALREADY_EXIST = "USER_ALREADY_EXIST";
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserDto createUser(UserDto userDto) {
    Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());

    if (existingUser.isPresent()) {
      throw new UserAlreadyExistException(
          new ErrorDto(
              USER_ALREADY_EXIST,
              String.format("User with email: %s, already exists", userDto.getEmail())));
    }
    log.info("Creating user for email: {}", userDto.getEmail());

    User newUser = userMapper.map(userDto);
    User savedUser = userRepository.save(newUser);
    return userMapper.map(savedUser);
  }
}
