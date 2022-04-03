package com.example.demo.service;

import com.example.demo.dto.Post;
import com.example.demo.dto.PostDto;
import com.example.demo.dto.PostResponse;

import java.util.List;


public interface PostService {

    List<Post> getPosts();

    PostDto createPost(PostResponse postResponse);
    PostResponse getPostDetails(String postId);
    PostDto updatePost(String postId, PostResponse postResponse);
    String deletePost(String postId);




}