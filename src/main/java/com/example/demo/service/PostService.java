package com.example.demo.service;


import com.example.demo.dto.PostRequest;
import com.example.demo.model.Post;
import com.example.demo.dto.PostResponse;

import java.util.List;


public interface PostService {


    List<PostResponse> getPosts(Integer page, Integer size);
    PostResponse createPost(PostRequest postRequest);
    PostResponse getPostDetails(String id);
    PostResponse updatePost(String id, PostRequest postRequest);
    String deletePost(String postId);



}



