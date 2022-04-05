package com.example.demo.service;


import com.example.demo.dto.UserDto;
import com.example.demo.feign.CommentFeign;
import com.example.demo.feign.LikeFeign;
import com.example.demo.model.Post;
import com.example.demo.dto.PostResponse;
import com.example.demo.feign.UserFeign;
import com.example.demo.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public List<PostResponse> getPosts(Integer page, Integer size) {

        page = Optional.ofNullable(page).orElse(0);
        size = Optional.ofNullable(size).orElse(10);
        Pageable paging = PageRequest.of(page, size);
        Page<Post> allPosts = postRepo.findAll(paging);
        List<PostResponse> postResponses = new ArrayList<>();
        for(Post post:allPosts){
            PostResponse newPostResponse = new PostResponse();
            newPostResponse.setCommentsCount(commentFeign.getCommentsCount((post.getPostId()))); //get the comment count
            newPostResponse.setLikesCount(likeFeign.getLikesCount(post.getPostId()));
            newPostResponse.setPostId(post.getPostId());
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
    public PostResponse createPost(Post post) {

        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());
        Post newPost = postRepo.save(new Post(post.getPostId(), post.getPost(), post.getPostedBy(), post.getCreatedAt(), post.getUpdatedAt()));
        PostResponse postResponse = new PostResponse();
        postResponse.setPost(post.getPost()); //setting the post
        postResponse.setCommentsCount(0); //no. of counts
        postResponse.setLikesCount(0);     //no. of likes
        postResponse.setCreatedAt(post.getCreatedAt());    //date created
        postResponse.setUpdatedAt(post.getUpdatedAt());    // date updated
        postResponse.setPostId(newPost.getPostId());
        UserDto postedByUser = userFeign.getUserDetails(post.getPostedBy());
        postResponse.setPostedByUser(postedByUser);
       // System.out.println("The user info : :" +postedByUser);
        return postResponse;
    }

    @Override
    public PostResponse getPostDetails(String postId) {

        Post post = postRepo.findById(postId).get();
        PostResponse newPostResponse = new PostResponse();
        newPostResponse.setCommentsCount(commentFeign.getCommentsCount((postId))); //get the comment count
        newPostResponse.setLikesCount(likeFeign.getLikesCount(postId));
        newPostResponse.setPostId(post.getPostId());
        newPostResponse.setCreatedAt(post.getCreatedAt());
        newPostResponse.setUpdatedAt(post.getUpdatedAt());
        newPostResponse.setPost(post.getPost());
        UserDto postedByUser = userFeign.getUserDetails(post.getPostedBy());
        newPostResponse.setPostedByUser(postedByUser);
        postRepo.save(post);
        return newPostResponse;
    }


    @Override
    public Post updatePost(String postId, Post post) {
        Post postToBeUpdated = postRepo.findByPostId(postId);
        postToBeUpdated.setPost(post.getPost());
        postToBeUpdated.setUpdatedAt(new Date());
        return postRepo.save(postToBeUpdated);
    }

    @Override
    public Post deletePost(String id) {

        Post postToBeDeleted = postRepo.findByPostId(id);
        return postRepo.deleteByPostId(postToBeDeleted.getPostId());
    }

}



