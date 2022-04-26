package com.example.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="comment-service")
public interface CommentFeign {

    @GetMapping("/posts/{postId}/comments/count")
    public Integer getCommentsCount(@PathVariable("postId") String postId);


}
