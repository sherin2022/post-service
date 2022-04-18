package com.example.demo.service;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.CustomFeignException;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.feign.CommentFeign;
import com.example.demo.feign.LikeFeign;
import com.example.demo.model.Post;
import com.example.demo.dto.PostResponse;
import com.example.demo.dto.PostRequest;
import com.example.demo.feign.UserFeign;
import com.example.demo.repo.PostRepo;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.demo.constantException.ConstantException.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepo postRepo;

    @Autowired
    CommentFeign commentFeign;

    @Autowired
    LikeFeign likeFeign;

    @Autowired
    UserFeign userFeign;


    @Override
    public List<PostResponse> getPosts(Integer page, Integer pageSize) {

        page = Optional.ofNullable(page).orElse(0);
        pageSize = Optional.ofNullable(pageSize).orElse(10);
        Pageable paging = PageRequest.of(page, pageSize);
        Page<Post> allPosts = postRepo.findAll(paging);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : allPosts) {
            PostResponse newPostResponse = new PostResponse();
            newPostResponse.setCommentsCount(commentFeign.getCommentsCount((post.getId()))); //get the comment count
            newPostResponse.setLikesCount(likeFeign.getLikesCount(post.getId()));
            newPostResponse.setId(post.getId());
            newPostResponse.setCreatedAt(post.getCreatedAt());
            newPostResponse.setUpdatedAt(post.getUpdatedAt());
            newPostResponse.setPost(post.getPost());
            UserDto postedByUser = userFeign.getUserDetails(post.getPostedBy());
            newPostResponse.setPostedByUser(postedByUser);
            postResponses.add(newPostResponse);
        }
        return postResponses;
    }

    @Override
    public PostResponse createPost(PostRequest postRequest) {

        Post newPost = postRepo.save(new Post(postRequest.getId(), postRequest.getPost(), postRequest.getPostedBy(),new Date(),new Date()));
        PostResponse postResponse = new PostResponse();
        postResponse.setPost(postRequest.getPost()); //setting the post
        postResponse.setCommentsCount(0); //no. of counts
        postResponse.setLikesCount(0);     //no. of likes
        postResponse.setCreatedAt(newPost.getCreatedAt());    //date created
        postResponse.setUpdatedAt(newPost.getUpdatedAt());    // date updated
        postResponse.setId(newPost.getId());
        UserDto postedByUser = userFeign.getUserDetails(postRequest.getPostedBy());
        postResponse.setPostedByUser(postedByUser);
        return postResponse;
    }

    @Override
    public PostResponse getPostDetails(String postId) {

        Post post = postRepo.findById(postId).get();
        PostResponse newPostResponse = new PostResponse();
        newPostResponse.setCommentsCount(commentFeign.getCommentsCount((postId))); //get the comment count
        newPostResponse.setLikesCount(likeFeign.getLikesCount(postId));
        newPostResponse.setId(post.getId());
        newPostResponse.setCreatedAt(post.getCreatedAt());
        newPostResponse.setUpdatedAt(post.getUpdatedAt());
        newPostResponse.setPost(post.getPost());
        UserDto postedByUser = userFeign.getUserDetails(post.getPostedBy());
        newPostResponse.setPostedByUser(postedByUser);
        return newPostResponse;
    }

    @Override
    public PostResponse updatePost(String postId, PostRequest postRequest) {
        Post postToBeUpdated;
        try {
            Optional<Post> post = postRepo.findById(postId);
            if(post.isPresent()) {
            postToBeUpdated = postRepo.findById(postId).get();
            postToBeUpdated.setPost(postRequest.getPost());
            postToBeUpdated.setUpdatedAt(new Date());
            postRepo.save(postToBeUpdated);

            PostResponse updatedPostResponse = new PostResponse();
            updatedPostResponse.setCommentsCount(commentFeign.getCommentsCount((postToBeUpdated.getId()))); //get the comment count
            updatedPostResponse.setLikesCount(likeFeign.getLikesCount(postToBeUpdated.getId()));
            updatedPostResponse.setId(postToBeUpdated.getId());
            updatedPostResponse.setCreatedAt(postToBeUpdated.getCreatedAt());
            updatedPostResponse.setUpdatedAt(postToBeUpdated.getUpdatedAt());
            updatedPostResponse.setPost(postToBeUpdated.getPost());
            UserDto postToBeUpdatededByUser = userFeign.getUserDetails(postToBeUpdated.getPostedBy());
            updatedPostResponse.setPostedByUser(postToBeUpdatededByUser);

            return updatedPostResponse;
            }  else{
            throw new PostNotFoundException(POSTNOTFOUND + postId);}
        }
        catch (FeignException | HystrixRuntimeException e){
            throw new CustomFeignException(FEIGN_EXCEPTION);
        }
    }


    @Override
    public String deletePost(String id) {

        try {
            Post postToBeDeleted = postRepo.findById(id).get();
             postRepo.deleteById(postToBeDeleted.getId());
             return DELETEPOST;
        }
        catch (Exception e)
        {
            throw new PostNotFoundException(POSTNOTFOUND + id);
        }
    }




}

