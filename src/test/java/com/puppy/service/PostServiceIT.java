package com.puppy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import com.github.database.rider.core.api.dataset.DataSet;
import com.puppy.AbstractIT;
import com.puppy.dto.PostDto;
import com.puppy.dto.PostResponseDto;
import com.puppy.exception.PostNotFoundException;
import com.puppy.exception.UserNotFoundException;
import com.puppy.model.Post;
import com.puppy.model.User;
import com.puppy.repository.PostRepository;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class PostServiceIT extends AbstractIT {

  private static final String DESCRIPTION = "Some stuff";
  private static final String USER_EMAIL = "Paul@domain.com";
  private static final String USER_NAME = "Paul";
  private static final String IMAGE_URL = "http://localhost:8080/api/v1/images/1_213.png";

  private static final ZonedDateTime CREATED_DATE = ZonedDateTime.now();

  @Autowired private PostService postService;
  @Autowired private PostRepository postRepository;
  @MockitoBean private LocalStorageService localStorageService;

  @Test
  @DataSet("/datasets/PostServiceIT/user_creating_post.xml")
  void createPost_givenInputInformation_postIsCreated() {
    // given
    PostDto postDto =
        PostDto.builder()
            .id(1)
            .description(DESCRIPTION)
            .createdDate(CREATED_DATE)
            .userEmail(USER_EMAIL)
            .build();
    MockMultipartFile file = new MockMultipartFile("testFile.png", "test".getBytes());
    doNothing().when(localStorageService).storeImage(any(), anyString());
    // when
    PostDto response = postService.createPost(postDto, file);
    // then
    doInTransaction(
        t -> {
          assertThat(response).isNotNull();

          List<Post> posts = postRepository.findAll();
          assertThat(posts).isNotNull().hasSize(1);

          Post savedPost = posts.get(0);
          User user = savedPost.getUser();

          assertThat(savedPost).isNotNull();
          assertThat(savedPost.getImageName()).isNotNull();
          assertThat(user).isNotNull();
          assertThat(user.getPosts()).hasSize(1);
        });
  }

  @Test
  void createPost_userDoesNotExist_exceptionIsThrown() {
    // given
    PostDto postDto =
        PostDto.builder()
            .id(1)
            .description(DESCRIPTION)
            .createdDate(CREATED_DATE)
            .userEmail(USER_EMAIL)
            .build();
    MockMultipartFile file = new MockMultipartFile("testFile", "test".getBytes());
    doNothing().when(localStorageService).storeImage(any(), anyString());
    // when
    UserNotFoundException response =
        assertThrows(UserNotFoundException.class, () -> postService.createPost(postDto, file));

    // then
    assertThat(response.getMessage()).isEqualTo("Provided User does not exists");
  }

  @Test
  @DataSet("/datasets/PostServiceIT/user_with_post.xml")
  void getPost_postExists_postInformationIsReturned() {
    // given
    long postId = 12L;
    // when
    PostResponseDto postResponseDto = postService.getPost(postId);
    // then
    assertThat(postResponseDto).isNotNull();
    assertThat(postResponseDto.getId()).isEqualTo(postId);
    assertThat(postResponseDto.getUserName()).isEqualTo(USER_NAME);
    assertThat(postResponseDto.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(postResponseDto.getImageURL()).isEqualTo(IMAGE_URL);
  }

  @Test
  void getPost_postNotExist_exceptionIsThrown() {
    // given
    long postId = 12L;
    // when
    PostNotFoundException response =
        assertThrows(PostNotFoundException.class, () -> postService.getPost(postId));

    // then
    assertThat(response.getMessage()).isEqualTo("Provided User does not have posts");
  }

  @Test
  @DataSet("/datasets/PostServiceIT/user_with_posts.xml")
  void getPostsByUser_postsExists_postInformationIsReturned() {
    // given
    long userId = 1212L;
    // when
    List<PostResponseDto> postResponseDto = postService.getPostsByUser(userId);
    // then
    assertThat(postResponseDto).isNotNull().hasSize(4);
    assertThat(postResponseDto).extracting(PostResponseDto::getUserName).containsOnly(USER_NAME);
  }
}
