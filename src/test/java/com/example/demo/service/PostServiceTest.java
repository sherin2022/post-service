
package com.example.demo.service;

import com.example.demo.constantException.ConstantException;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.CustomFeignException;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.feign.CommentFeign;
import com.example.demo.feign.LikeFeign;
import com.example.demo.feign.UserFeign;
import com.example.demo.model.Post;
import com.example.demo.repo.PostRepo;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.Constants;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostServiceTest {
    @Autowired
    @InjectMocks
    PostServiceImpl postService;
    @MockBean
    PostRepo postRepo;
    @MockBean
    UserFeign userFeign;
    @MockBean
    CommentFeign commentFeign;
    @MockBean
    LikeFeign likeFeign;

    @Test
    void testDeletePostById() {

        when(this.userFeign.getUserDetails((String) any())).thenThrow(mock(FeignException.class));
        Post post = new Post();
        post.setId("1");
        post.setPost("posted by");
        post.setCreatedAt(new Date());
        post.setPostedBy("1");
        post.setUpdatedAt(new Date());
        when(this.postRepo.findById((String) any())).thenReturn(Optional.of(post));
        doNothing().when(this.postRepo).deleteById((String) any());
        this.postService.deletePost("1");

    }
    @Test
    void testExceptionThrownWhenIdNotFound() {
        Mockito.doThrow(PostNotFoundException.class).when(postRepo).deleteById(any());
        Exception userNotFoundException = assertThrows(PostNotFoundException.class, () -> postService.deletePost("1"));
        assertTrue(userNotFoundException.getMessage().contains(ConstantException.POSTNOTFOUND));
    }

    @Test
    void testCreatePost() {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());

        PostRequest postRequest = new PostRequest("1", "Post", "Posted By");
        Post post = new Post();
        post.setCreatedAt(new Date());
        post.setId("1");
        post.setPost("Post");
        post.setPostedBy("Posted By");
        post.setUpdatedAt(new Date());
        when(this.postRepo.save((Post) any())).thenReturn(post);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);
        when(this.commentFeign.getCommentsCount((String) any())).thenThrow(mock(FeignException.class));
        this.postService.createPost(postRequest);
    }
    @Test
    void testGetPostDetailById() {
        Post post = new Post();
        post.setId("1");
        post.setPost("Hi");

        Mockito.when(postRepo.findById("1")).thenReturn(Optional.ofNullable(post));
        assertThat(postService.getPostDetails(post.getId()));
        assertThrows(NoSuchElementException.class, () -> postService.getPostDetails(null));
    }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueInGetPostDetailByID() {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());

        Post post = new Post();
        post.setCreatedAt(new Date());
        post.setId("1");
        post.setPost("Post");
        post.setPostedBy("Posted By");
        post.setUpdatedAt(new Date());
        Optional<Post> ofResult = Optional.of(post);
        when(this.postRepo.findById((String) any())).thenReturn(ofResult);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);
        this.postService.getPostDetails("1");
    }

    @Test
    void testUpdatePostById() {
        Post post1 = createOnePost();
        PostResponse postResponse = createOnePostToResponse1();
        PostRequest postRequest = createOnePostToRequest();
        Mockito.when(postRepo.findById("1")).thenReturn(Optional.ofNullable(post1));
        assertThat(postService.updatePost("1", postRequest));
        assertThrows(PostNotFoundException.class, () -> postService.updatePost(null, postRequest));
    }
    private Post createOnePost() {
        Post post1 = new Post();
        post1.setId("1");
        post1.setPost("Hi");
        post1.setPostedBy("1");
        post1.setCreatedAt(new Date());
        post1.setUpdatedAt(new Date());
        return post1;
    }
    private PostResponse createOnePostToResponse1() {
        PostResponse postResponse = new PostResponse();
        UserDto userDto = new UserDto();
        postResponse.setId("1");
        postResponse.setPost("Hi");
        postResponse.setPostedByUser(userDto);
        postResponse.setCreatedAt(new Date());
        postResponse.setUpdatedAt(new Date());
        postResponse.setCommentsCount(commentFeign.getCommentsCount("1"));
        postResponse.setLikesCount(likeFeign.getLikesCount("1"));
        return postResponse;
    }
    private PostRequest createOnePostToRequest() {
        PostRequest postRequest = new PostRequest();
        postRequest.setPost("Hi");
        postRequest.setPostedBy("2");
        return postRequest;
    }
    @Test
    void testExceptionThrownWhenFeignConnectionIssueInUpdatePostById() {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());

        Post post = new Post();
        post.setCreatedAt(new Date());
        post.setId("1");
        post.setPost("Post");
        post.setPostedBy("Posted By");
        post.setUpdatedAt(new Date());
        Optional<Post> ofResult = Optional.of(post);

        PostRequest postRequest = new PostRequest("1", "Post", "Posted By");
        Post post1 = new Post();
        post1.setCreatedAt(new Date());
        post1.setId("1");
        post1.setPost("Post");
        post1.setPostedBy("Posted By");
        post1.setUpdatedAt(new Date());
        when(this.postRepo.save((Post) any())).thenReturn(post1);
        when(this.postRepo.findById((String) any())).thenReturn(ofResult);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);
        when(this.commentFeign.getCommentsCount((String) any())).thenThrow(mock(FeignException.class));
        assertThrows(CustomFeignException.class, () -> this.postService.updatePost("1", postRequest));
    }

    @Test
    void testGetAllPosts() {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());

        Post post = new Post();
        post.setCreatedAt(new Date());
        post.setId("1");
        post.setPost("Post");
        post.setPostedBy("Posted By");
        post.setUpdatedAt(new Date());

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(postList);
        when(this.postRepo.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(pageImpl);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);
        when(this.commentFeign.getCommentsCount((String) any())).thenReturn(3);
        assertEquals(1, this.postService.getPosts(1, 3).size());
    }

    @Test
    void testExceptionThrownWhenFeignConnectionFailed() {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());

        Post post = new Post();
        post.setCreatedAt(new Date());
        post.setId("1");
        post.setPost("Post");
        post.setPostedBy("Posted By");
        post.setUpdatedAt(new Date());

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(postList);
        when(this.postRepo.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(pageImpl);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);
        when(this.commentFeign.getCommentsCount((String) any())).thenThrow(mock(FeignException.class));
        assertThrows(FeignException.class, () -> this.postService.getPosts(1, 3));
    }

}

