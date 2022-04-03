package com.example.demo.fegin;

import com.example.demo.dto.User;
import com.example.demo.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="http://localhost:3005")
public interface UserFeign {
    @GetMapping("/users/{userId}")
    String getUserById(@PathVariable("userId") String userId);

}
