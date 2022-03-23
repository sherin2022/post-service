package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.model.PostResponse;
import com.example.demo.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<Post>>getPosts(){
        return new ResponseEntity<List<Post>>(postService.getPosts(), HttpStatus.FOUND);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse>createPost(@RequestBody Post post){
        return new ResponseEntity<PostResponse>(postService.createPost(post),HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post>get(@PathVariable("postId") String id){
        return new ResponseEntity<Post>(postService.getPostDetails(id),HttpStatus.CREATED);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Post>updatePost(@PathVariable("postId")String id, @RequestBody Post post){
        return new ResponseEntity<Post>(postService.updatePost(id,post),HttpStatus.FOUND);
    }
    @DeleteMapping("posts/{postId}")
    public ResponseEntity<Post> deleteUser(@PathVariable("postId") String postId){
        return new ResponseEntity<Post>(postService.deletePost(postId),HttpStatus.FOUND);
    }

}