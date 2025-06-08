package com.puppy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.puppy.dto.PostDto;
import com.puppy.dto.PostResponseDto;
import com.puppy.model.Post;
import com.puppy.model.User;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PostMapperImpl.class})
class PostMapperTest {

  private static final String DESCRIPTION = "Some stuff";
  private static final String USER_EMAIL = "Paul@domain.com";
  private static final String USER_NAME = "Paul";
  private static final String IMAGE_PATH = "SomeName";
  private static final String IMAGE_URL = "http://someurl.gts";
  private static final ZonedDateTime CREATED_DATE = ZonedDateTime.now();

  @Autowired private PostMapper postMapper;

  @Test
  void map_givenPostDto_returnAValidPostEntity() {
    // given
    PostDto postDto =
        PostDto.builder()
            .id(1)
            .description(DESCRIPTION)
            .createdDate(CREATED_DATE)
            .userEmail(USER_EMAIL)
            .build();
    // when
    Post post = postMapper.map(postDto, User.builder().build());
    // then
    assertThat(post).isNotNull();
    assertThat(post.getId()).isNull();
    assertThat(post.getImageName()).isNull();
    assertThat(post.getUser()).isNotNull();
    assertThat(post.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(post.getCreatedDate()).isEqualTo(CREATED_DATE);
  }

  @Test
  void map_givenPostEntity_returnAValidPostDto() {
    // given
    Post post =
        Post.builder()
            .id(1L)
            .description(DESCRIPTION)
            .createdDate(CREATED_DATE)
            .imageName(IMAGE_PATH)
            .user(User.builder().build())
            .build();
    // when
    PostDto postDto = postMapper.map(post, USER_EMAIL);
    // then
    assertThat(postDto).isNotNull();
    assertThat(postDto.getId()).isEqualTo(1);
    assertThat(postDto.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(postDto.getCreatedDate()).isEqualTo(CREATED_DATE);
    assertThat(postDto.getUserEmail()).isEqualTo(USER_EMAIL);
  }

  @Test
  void mapPostResponse_givenInputInformation_returnAValidPostResponseDto() {
    // given
    Post post =
        Post.builder()
            .id(1L)
            .description(DESCRIPTION)
            .createdDate(CREATED_DATE)
            .imageName(IMAGE_PATH)
            .user(User.builder().build())
            .build();
    // when
    PostResponseDto postResponseDto = postMapper.mapPostResponse(post, USER_NAME, IMAGE_URL);
    // then
    assertThat(postResponseDto).isNotNull();
    assertThat(postResponseDto.getId()).isEqualTo(1);
    assertThat(postResponseDto.getUserName()).isEqualTo(USER_NAME);
    assertThat(postResponseDto.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(postResponseDto.getCreatedDate()).isEqualTo(CREATED_DATE);
    assertThat(postResponseDto.getImageURL()).isEqualTo(IMAGE_URL);
  }
}
