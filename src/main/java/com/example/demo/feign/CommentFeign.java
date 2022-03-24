package com.example.demo.feign;

import com.example.demo.model.Comment;
import com.example.demo.dto.CommentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="comment-service", fallbackFactory = HystrixFallbackFactory.class)
public interface CommentFeign {

    @GetMapping("/posts/{postId}/comments/count")
    public Long getCommentsCount(@PathVariable("postId") String postId);

    @PostMapping("/posts/{postId}/comments")
    public CommentResponse createComment(@RequestBody Comment comment);


}
