package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String id;
    @NotNull(message = "post should not be empty")
    @NotEmpty(message = "post should not be empty")
    private String post;
    private String postedBy;//User Id who posted.
    private Integer likesCount;
    private Integer commentsCount;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}