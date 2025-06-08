package com.puppy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.puppy.dto.UserDto;
import com.puppy.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserMapperImpl.class})
class UserMapperTest {
  private static final String USER_NAME = "Paul";
  private static final String USER_EMAIL = "Paul@domain.com";

  @Autowired private UserMapper userMapper;

  @Test
  void map_givenUserDto_returnAValidUserEntity() {
    // given
    UserDto userDto = UserDto.builder().id(1).name(USER_NAME).email(USER_EMAIL).build();
    // when
    User user = userMapper.map(userDto);
    // then
    assertThat(user).isNotNull();
    assertThat(user.getId()).isNull();
    assertThat(user.getName()).isEqualTo(USER_NAME);
    assertThat(user.getEmail()).isEqualTo(USER_EMAIL);
  }

  @Test
  void map_givenUserEntity_returnAValidUserDto() {
    // given
    User user = User.builder().id(1L).name(USER_NAME).email(USER_EMAIL).build();
    // when
    UserDto userDto = userMapper.map(user);
    // then
    assertThat(userDto).isNotNull();
    assertThat(userDto.getId()).isEqualTo(1);
    assertThat(userDto.getName()).isEqualTo(USER_NAME);
    assertThat(userDto.getEmail()).isEqualTo(USER_EMAIL);
  }
}
