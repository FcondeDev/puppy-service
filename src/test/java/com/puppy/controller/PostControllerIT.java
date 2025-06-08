package com.puppy.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.puppy.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PostControllerIT {

  @Value("${app.security.api.key}")
  private String apiKey;

  @Autowired private MockMvc mvc;

  @MockitoBean private PostService postService;

  @Test
  void getPost_invalidApiKey_returnsAuthorizationError() throws Exception {

    mvc.perform(get("/api/v1/posts/1").header("X-API-KEY", "fakeKey"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.errorCode").value("INVALID_API_KEY"))
        .andExpect(jsonPath("$.description").value("Provide API Key is not valid"));

    verifyNoInteractions(postService);
  }

  @Test
  void getPost_validApiKey_returnsOk() throws Exception {

    mvc.perform(get("/api/v1/posts/1").header("X-API-KEY", apiKey)).andExpect(status().isOk());

    verify(postService).getPost(1L);
  }
}
