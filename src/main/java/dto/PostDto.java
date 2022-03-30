package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String id;
    private String post;
    private String postedBy;
    private Date createdAt;
    private Date updatedAt;
    private int likesCount; //This will come like-service -> feign operation
    private int commentsCount; //This will come comment-service -> feign operation
    private String userName;
}
