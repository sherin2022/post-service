package com.example.demo.controller;

import com.example.demo.constantException.ConstantException;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.enums.BloodGroup;
import com.example.demo.enums.Gender;
import com.example.demo.exception.PostIdMismatchException;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.net.ssl.SSLSession;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.demo.enums.BloodGroup.A_NEG;
import static com.example.demo.enums.Gender.FEMALE;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(PostController.class)
public class PostControllerTest {
    @MockBean
    PostService postService;
    @Autowired
    MockMvc mockMvc;

    public static String asJsonString(final Object object){
        try{
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   // creating new post
    @Test
    void createPostTest() throws Exception {


        PostRequest postRequest = createNewPost();
        PostResponse postResponse = createPostDto();
        Mockito.when(postService.createPost(postRequest)).thenReturn(postResponse);
        mockMvc.perform(post("/posts")
                        .content(asJsonString(postResponse))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    private PostResponse createPostDto() {

       Date dateAt = new Date();
        UserDto userDto = new UserDto();
        PostResponse postResponse = new PostResponse("1","Hi All",userDto,0,0,dateAt,dateAt);
        return postResponse;

    }

    private PostRequest createNewPost() {
        PostRequest postRequest=new PostRequest();
        postRequest.setId("1");
        postRequest.setPost("Sagar");
        postRequest.setPostedBy("62456ed2cfefbf595a5a66d7");
        return postRequest;
    }

    //Get All post
   @Test
   void testGetAllPosts() throws Exception {
       List<PostResponse> postResponses = createPostsList();
       Mockito.when(postService.getPosts(0, 2)).thenReturn(postResponses);
       mockMvc.perform(get("/posts?page=0&pageSize=2"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", Matchers.hasSize(2)))
               .andExpect(jsonPath("$[0].post", Matchers.is("FirstPost")))
               .andExpect(jsonPath("$[1].post", Matchers.is("SecondPost")));
   }
    private List<PostResponse> createPostsList() {
        List<PostResponse> posts = new ArrayList<>();

        PostResponse post1 = new PostResponse();
        post1.setId("1");
        post1.setPost("FirstPost");
        Date dob = new Date();
        post1.setPostedByUser(new UserDto("1", "savita", "G", "B", "9090909090", "savita@mail.com", "Bangalore",dob, "001",A_NEG, FEMALE));
        post1.setCreatedAt(null);
        post1.setUpdatedAt(null);
        post1.setLikesCount(3);
        post1.setCommentsCount(2);

        PostResponse post2 = new PostResponse();
        post2.setId("2");
        post2.setPost("SecondPost");
        post2.setPostedByUser(new UserDto("2", "kavita", "G", "B", "9090909090", "savita@mail.com", "Bangalore",dob, "123", A_NEG, FEMALE));
        post2.setCreatedAt(null);
        post2.setUpdatedAt(null);
        post2.setLikesCount(3);
        post2.setCommentsCount(2);

        posts.add(post1);
        posts.add(post2);
        return posts;
    }
   //Get post by Id test case
    @Test
    void testGetPostByID() throws Exception {
        PostResponse postResponse = createOnePost();
        Mockito.when(postService.getPostDetails("1")).thenReturn(postResponse);
        mockMvc.perform(get("/posts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(7)))
                .andExpect(jsonPath("$.post", Matchers.is("PostTest")));
    }
    private PostResponse createOnePost() {
        PostResponse postResponse = new PostResponse();
        postResponse.setId("2");
        postResponse.setPost("PostTest");
        Date dob = new Date();
        postResponse.setPostedByUser(new UserDto("1", "savita", "G", "B", "9090909090", "savita@mail.com", "Bangalore",dob ,"123", A_NEG, FEMALE));
        return postResponse;
    }

   //Update post test case
    @Test
    void testUpdatePostById() throws Exception {
        PostResponse postResponse = createOnePostToUpdate();
        PostRequest postRequest = new PostRequest();
        when(postService.updatePost("1", postRequest)).thenReturn(postResponse);
        mockMvc.perform(put("/posts/1")
                        .content(asJsonString(postResponse))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

    }
    private PostResponse createOnePostToUpdate() {
        PostResponse postResponse = new PostResponse();
        postResponse.setId("1");
        postResponse.setPost("PostTest");
        postResponse.setLikesCount(2);
        postResponse.setCommentsCount(2);
        postResponse.setCreatedAt(new Date());
        postResponse.setUpdatedAt(new Date());
        Date dob = new Date();
        postResponse.setPostedByUser(new UserDto("1", "savita", "G", "B", "9090909090", "savita@mail.com", "Bangalore",dob, "123",A_NEG,FEMALE));
        return postResponse;
    }
    @Test
    void testExceptionThrownWhenIdNotFound() throws Exception {
        PostController postController = new PostController();
        PostRequest postRequest = mock(PostRequest.class);
        when(postRequest.getId()).thenReturn("a");
        assertThrows(NullPointerException.class, () -> postController.updatePost("1", postRequest));

    }

   


  //Delete test case
  @Test
  void testDeletePostById() throws Exception {
      PostResponse  postResponse = createOnePost();
      Mockito.when(postService.deletePost("1")).thenReturn(String.valueOf(postResponse));
      this.mockMvc.perform(MockMvcRequestBuilders
                      .delete("/posts/1")
                      .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
              .andDo(print());

  }

}
