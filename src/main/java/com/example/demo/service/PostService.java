package com.example.demo.service;

import com.example.demo.model.Post;
import com.example.demo.model.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PostService {

    List<Post> getPosts();
    PostResponse createPost(Post post);
    Post getPostDetails(String id);
    Post updatePost(String id, Post post);
    Post deletePost(String postId);

}
