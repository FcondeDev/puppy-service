package com.puppy.mapper;

import com.puppy.dto.UserDto;
import com.puppy.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto map(User user);

  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "posts")
  User map(UserDto userDto);
}
