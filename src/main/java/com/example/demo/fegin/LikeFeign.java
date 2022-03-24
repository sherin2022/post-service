package com.example.demo.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="like-service", fallbackFactory = HystrixFallbackFactory.class)
public interface LikeFeign {

    @GetMapping("/postsOrComments/{postOrCommentId}/likes/count")
    public Integer getLikesCount(@PathVariable("postOrCommentId") String postOrCommentId);
}
