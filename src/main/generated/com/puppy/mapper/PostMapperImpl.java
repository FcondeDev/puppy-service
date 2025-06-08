package com.puppy.mapper;

import com.puppy.dto.PostDto;
import com.puppy.dto.PostResponseDto;
import com.puppy.model.Post;
import com.puppy.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-07T20:15:50-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDto map(Post post, String userEmail) {
        if ( post == null && userEmail == null ) {
            return null;
        }

        PostDto.PostDtoBuilder postDto = PostDto.builder();

        if ( post != null ) {
            if ( post.getId() != null ) {
                postDto.id( post.getId() );
            }
            postDto.description( post.getDescription() );
            postDto.createdDate( post.getCreatedDate() );
        }
        postDto.userEmail( userEmail );

        return postDto.build();
    }

    @Override
    public Post map(PostDto postDto, User user) {
        if ( postDto == null && user == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        if ( postDto != null ) {
            if ( postDto.getCreatedDate() != null ) {
                post.createdDate( postDto.getCreatedDate() );
            }
            else {
                post.createdDate( java.time.ZonedDateTime.now() );
            }
            post.description( postDto.getDescription() );
        }
        post.user( user );

        return post.build();
    }

    @Override
    public PostResponseDto mapPostResponse(Post post, String userName, String imageURL) {
        if ( post == null && userName == null && imageURL == null ) {
            return null;
        }

        PostResponseDto.PostResponseDtoBuilder postResponseDto = PostResponseDto.builder();

        if ( post != null ) {
            if ( post.getId() != null ) {
                postResponseDto.id( post.getId() );
            }
            postResponseDto.description( post.getDescription() );
            postResponseDto.createdDate( post.getCreatedDate() );
        }
        postResponseDto.userName( userName );
        postResponseDto.imageURL( imageURL );

        return postResponseDto.build();
    }
}
