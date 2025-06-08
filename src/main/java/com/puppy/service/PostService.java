package com.puppy.service;

import com.puppy.dto.ErrorDto;
import com.puppy.dto.PostDto;
import com.puppy.dto.PostResponseDto;
import com.puppy.exception.PostNotFoundException;
import com.puppy.exception.UserNotFoundException;
import com.puppy.mapper.PostMapper;
import com.puppy.model.Post;
import com.puppy.model.User;
import com.puppy.repository.PostRepository;
import com.puppy.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
  private static final String POST_NOT_FOUND = "POST_NOT_FOUND";
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final StorageService storageService;
  private final PostMapper postMapper;

  @Value("${app.images.url}")
  private String imagesUrl;

  @Transactional
  public PostDto createPost(PostDto postDto, MultipartFile image) {
    Optional<User> existingUser = userRepository.findByEmail(postDto.getUserEmail());

    if (existingUser.isEmpty()) {
      throw new UserNotFoundException(
          new ErrorDto(USER_NOT_FOUND, "Provided User does not exists"));
    }

    User user = existingUser.get();
    Post post = postMapper.map(postDto, user);

    log.info("Creating post for user: {}", user.getEmail());

    String imageName = createImageName(user.getId(), image);

    post.setImageName(imageName);
    user.getPosts().add(post);

    storageService.storeImage(image, imageName);
    Post savedPost = postRepository.save(post);
    return postMapper.map(savedPost, user.getEmail());
  }

  public PostResponseDto getPost(long id) {
    Optional<Post> existingPost = postRepository.findById(id);

    if (existingPost.isEmpty()) {
      throw new PostNotFoundException(
          new ErrorDto(POST_NOT_FOUND, "Provided User does not have posts"));
    }

    Post post = existingPost.get();
    User user = post.getUser();

    log.info("Getting post for user: {}", user.getEmail());

    String imageUrl = createImageUrl(post.getImageName());

    return postMapper.mapPostResponse(post, user.getName(), imageUrl);
  }

  public List<PostResponseDto> getPostsByUser(long userId) {

    List<Post> postsByUser = postRepository.findPostsByUserId(userId);

    if (postsByUser.isEmpty()) {
      throw new PostNotFoundException(
          new ErrorDto(POST_NOT_FOUND, "Provided User does not have posts"));
    }

    log.info("Getting posts for userId: {}, number of posts: {}", userId, postsByUser.size());

    return postsByUser.stream()
        .map(
            post -> {
              User user = post.getUser();

              String imageUrl = createImageUrl(post.getImageName());
              return postMapper.mapPostResponse(post, user.getName(), imageUrl);
            })
        .toList();
  }

  private String createImageUrl(String imageName) {
    return imagesUrl + imageName;
  }

  private String createImageName(long userId, MultipartFile image) {
    String name =
        StringUtils.hasLength(image.getOriginalFilename()) ? image.getOriginalFilename() : ".jpg";
    String extension = name.substring(name.lastIndexOf("."));
    return userId + "_" + Math.abs(UUID.randomUUID().getMostSignificantBits()) + extension;
  }
}
