package com.puppy.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class ApiKeySecurityFilter extends OncePerRequestFilter {

  private static final String API_KEY_HEADER = "X-API-KEY";

  @Value("${app.security.api.key}")
  private String apiKey;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String requestApiKey = request.getHeader(API_KEY_HEADER);

    if (apiKey.equals(requestApiKey)) {
      var authenticationToken =
          new UsernamePasswordAuthenticationToken(
              requestApiKey, requestApiKey, Collections.emptyList());
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      filterChain.doFilter(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      PrintWriter writer = response.getWriter();
      writer.println(
          "{\"errorCode\":\"INVALID_API_KEY\",\"description\":\"Provide API Key is not valid\"}");
      writer.flush();
      writer.close();
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getRequestURI().startsWith(SecurityConfig.IMAGES_URL);
  }
}
