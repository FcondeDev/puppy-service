package com.puppy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.puppy.AbstractIT;
import com.puppy.dto.UserDto;
import com.puppy.exception.UserAlreadyExistException;
import com.puppy.model.User;
import com.puppy.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceIT extends AbstractIT {

  private static final String USER_NAME = "Paul";
  private static final String USER_EMAIL = "Paul@domain.com";

  @Autowired UserService userService;
  @Autowired UserRepository userRepository;

  @Test
  void createUser_givenInputInformation_userIsCreated() {
    // given
    UserDto userDto = UserDto.builder().name(USER_NAME).email(USER_EMAIL).build();
    // when
    UserDto response = userService.createUser(userDto);
    // then
    List<User> users = userRepository.findAll();
    assertThat(users).isNotNull().hasSize(1);
    User newUser = users.get(0);
    assertThat(newUser).isNotNull();
    assertThat(newUser.getId()).isNotNull();
    assertThat(newUser.getName()).isEqualTo(USER_NAME);
    assertThat(newUser.getEmail()).isEqualTo(USER_EMAIL);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(newUser.getId());
    assertThat(response.getName()).isEqualTo(USER_NAME);
    assertThat(response.getEmail()).isEqualTo(USER_EMAIL);
  }

  @Test
  @DataSet("/datasets/UserServiceIT/existing_user.xml")
  @ExpectedDataSet("/datasets/UserServiceIT/existing_user.xml")
  void createUser_userAlreadyExists_exceptionIsThrown() {
    // given
    UserDto userDto = UserDto.builder().name(USER_NAME).email(USER_EMAIL).build();
    // when
    UserAlreadyExistException response =
        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userDto));

    // then
    assertThat(response.getMessage()).isEqualTo("User with email: Paul@domain.com, already exists");
  }
}
