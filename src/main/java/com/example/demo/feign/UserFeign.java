package com.example.demo.feign;


import com.example.demo.dto.UserDto;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="user-service")
public interface UserFeign {


    @GetMapping("/users/{userId}")
    public UserDto getUserDetails(@PathVariable("userId") String userId);

    //testing feign
//    @GetMapping("/users/test/{userId}")
//    public ResponseEntity<String> testFeign();

}
