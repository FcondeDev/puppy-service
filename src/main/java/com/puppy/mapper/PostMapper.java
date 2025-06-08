package com.puppy.mapper;

import com.puppy.dto.PostDto;
import com.puppy.dto.PostResponseDto;
import com.puppy.model.Post;
import com.puppy.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
  @Mapping(source = "userEmail", target = "userEmail")
  PostDto map(Post post, String userEmail);

  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "imageName")
  @Mapping(source = "user", target = "user")
  @Mapping(defaultExpression = "java(java.time.ZonedDateTime.now())", target = "createdDate")
  Post map(PostDto postDto, User user);

  @Mapping(source = "userName", target = "userName")
  @Mapping(source = "imageURL", target = "imageURL")
  PostResponseDto mapPostResponse(Post post, String userName, String imageURL);
}
