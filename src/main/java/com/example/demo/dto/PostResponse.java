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
    private UserDto postedBy; //User Id who posted.
    private Integer likesCount;
    private Long commentsCount;
    private Date createdAt;
    private Date updatedAt;

}
