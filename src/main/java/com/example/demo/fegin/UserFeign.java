package com.example.demo.fegin;

import com.example.demo.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="user-service", fallbackFactory = HystrixFallbackFactory.class)
public interface UserFeign {
    @GetMapping("/users/{userId}")
    public User getUserDetails(@PathVariable("userId") String userId);

}
