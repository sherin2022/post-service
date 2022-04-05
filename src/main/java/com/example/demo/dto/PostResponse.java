package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private String postId;
    private String post;
    private UserDto postedByUser; //User Id who posted.
    private Integer likesCount;
    private Integer commentsCount;
    private Date createdAt;
    private Date updatedAt;

    //Changing the postedByUser to return String and not UserDto to check feign. changes 1

}
