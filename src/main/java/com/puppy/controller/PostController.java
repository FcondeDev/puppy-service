package com.puppy.controller;

import com.puppy.dto.PostDto;
import com.puppy.dto.PostResponseDto;
import com.puppy.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<PostDto> createPost(
      @RequestPart PostDto postDto, @RequestPart MultipartFile image) {
    return new ResponseEntity<>(postService.createPost(postDto, image), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostResponseDto> getPost(@PathVariable long id) {
    return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
  }

  @GetMapping("user/{userId}")
  public ResponseEntity<List<PostResponseDto>> getPostsByUser(@PathVariable long userId) {
    return new ResponseEntity<>(postService.getPostsByUser(userId), HttpStatus.OK);
  }
}
