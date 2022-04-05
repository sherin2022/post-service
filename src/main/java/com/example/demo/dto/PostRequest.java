package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @Id
    private String postId;
    private String post;
    private String postedBy;

}
