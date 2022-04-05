package com.example.demo.controller;


import com.example.demo.dto.PostRequest;
import com.example.demo.model.Post;
import com.example.demo.dto.PostResponse;
import com.example.demo.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    //To get all post all records.
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public List<PostResponse> getPosts(@RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "size", required = false) Integer size) {
        return postService.getPosts(page, size);
    }

    //To add a new post record.
    @PostMapping
    public PostResponse createPost(@RequestBody PostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    //To get a post for a particular Id.
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostDetails(@PathVariable("postId") String postId) {
        return new ResponseEntity<>(postService.getPostDetails(postId), HttpStatus.OK);
    }

    //To update a particular postId.
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable("postId") String id, @RequestBody PostRequest postRequest) {
        return new ResponseEntity<PostResponse>(postService.updatePost(id, postRequest), HttpStatus.FOUND);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") String id) {
        return new ResponseEntity<String>(postService.deletePost(id), HttpStatus.FOUND);
    }

}

