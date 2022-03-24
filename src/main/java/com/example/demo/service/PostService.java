package com.example.demo.service;

import com.example.demo.dto.Post;
import com.example.demo.dto.PostResponse;

import java.util.List;


public interface PostService {

    List<Post> getPosts();
    PostResponse createPost(Post post);
    PostResponse getPostDetails(String id);
    Post updatePost(String id, Post post);
    String deletePost(String postId);




}