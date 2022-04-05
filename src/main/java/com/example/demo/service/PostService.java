package com.example.demo.service;

import com.example.demo.model.Post;
import com.example.demo.dto.PostResponse;

import java.util.List;


public interface PostService {

    List<PostResponse> getPosts(Integer page, Integer size);
    PostResponse createPost(Post post);
    PostResponse getPostDetails(String id);
    Post updatePost(String id, Post post);
    Post deletePost(String postId);

    //Adding a method to test feign
//    String testFeign(String postId);




}
