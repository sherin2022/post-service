package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection="post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @Id
    private String id;
    @NotBlank(message = "post should not be blank")
    @NotNull(message = "post should not be null")
    private String post;
    private String postedBy;

}
