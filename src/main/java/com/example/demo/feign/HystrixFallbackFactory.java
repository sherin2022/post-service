package com.example.demo.feign;

import org.springframework.cloud.openfeign.FallbackFactory;

public class HystrixFallbackFactory implements FallbackFactory<CommentFeign> {
    @Override
    public CommentFeign create(Throwable cause) {

        System.out.println("fallback; reason was: " + cause.getMessage());
        return null;

    }
}