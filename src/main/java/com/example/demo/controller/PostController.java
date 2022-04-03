package com.example.demo.controller;

import com.example.demo.dto.Post;
import com.example.demo.dto.PostDto;
import com.example.demo.dto.PostResponse;
import com.example.demo.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    //To get all post all records.
    @GetMapping
    public ResponseEntity<List<Post>>getPosts(){
        return new ResponseEntity<List<Post>>(postService.getPosts(), HttpStatus.OK);
    }
    //To add a new post record.
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostResponse postResponse)
    {
        return new ResponseEntity<>(postService.createPost(postResponse), HttpStatus.CREATED);
    }
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostDetails(@PathVariable("postId") String postId)
    {
        return new ResponseEntity<>(postService.getPostDetails(postId), HttpStatus.OK);
    }
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable("postId") String postId, @Valid @RequestBody PostResponse postResponse)
    {
        return new ResponseEntity<>(postService.updatePost(postId, postResponse), HttpStatus.OK);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") String postId)
    {
        return new ResponseEntity<>(postService.deletePost(postId), HttpStatus.OK);
    }
}