package com.example.demo.model;
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
    private String postedBy; //User Id who posted.
    private Integer likesCount;
    private Integer commentsCount;
    private Date createdAt;
    private Date updatedAt;

}