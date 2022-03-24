package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Comment {

        @Id
        private String commentId;//This the user Id;
        private String comment;
        private String commentedBy;
        private String commentedOnPost;// This is the postid
        private Date createdAt;
        private Date updatedAt;

    }

