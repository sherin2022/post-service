package com.example.demo.service;
import com.example.demo.dto.User;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.fegin.CommentFeign;
import com.example.demo.fegin.LikeFeign;
import com.example.demo.fegin.UserFeign;

import com.example.demo.dto.Post;
import com.example.demo.dto.PostResponse;

import com.example.demo.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.example.demo.constantException.ConstantException.DELETEPOST;
import static com.example.demo.constantException.ConstantException.POSTNOTFOUND;

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
    public List<Post> getPosts() {
        return postRepo.findAll();
    }

    @Override
    public PostResponse createPost(Post post) {

        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());

        PostResponse postResponse= new PostResponse();

        postResponse.setPost(post.getPost());
        postResponse.setCommentsCount(0L);
        postResponse.setLikesCount(0);
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setUpdatedAt(post.getUpdatedAt());
        Post newPost = postRepo.save(new Post(post.getPostId(),post.getPost(),post.getPostedBy(),post.getCreatedAt(),post.getUpdatedAt()));
        postResponse.setPostId(newPost.getPostId());
        User postedByUser = userFeign.getUserDetails(post.getPostedBy());
        postResponse.setPostedBy(postedByUser);
        return postResponse;
    }

    @Override
    public PostResponse getPostDetails(String postId) {

           try{
            Post post = postRepo.findById(postId).get();
            PostResponse newPostResponse = new PostResponse();
            // post.setLikesCount(likeFeign.getLikesCount(postId).getBody());
            newPostResponse.setCommentsCount(commentFeign.getCommentsCount((postId))); //get the comment count
            newPostResponse.setLikesCount(likeFeign.getLikesCount(postId));
            newPostResponse.setPostId(post.getPostId());
            newPostResponse.setPostedBy(userFeign.getUserDetails(postId));
            newPostResponse.setCreatedAt(post.getCreatedAt());
            newPostResponse.setUpdatedAt(post.getUpdatedAt());
            newPostResponse.setPost(post.getPost());
            postRepo.save(post);
            return newPostResponse;
        }
        catch (Exception e){
            throw new PostNotFoundException(POSTNOTFOUND + postId);
        }

    }

    //    @Override
//    public Post updatePost(String id, Post post) {
//        return postRepo.save(new Post(post.getPostId(),post.getPost(),post.getPostedBy(),post.getCreatedAt(),post.getUpdatedAt()));
//    }
    @Override
    public Post updatePost(String postId, Post post) {
        try {
            Post postToBeUpdated = postRepo.findByPostId(postId);
            postToBeUpdated.setPost(post.getPost());
            //postToBeUpdated.setPostedBy(post.getPostedBy());
            postToBeUpdated.setUpdatedAt(new Date());
            return postRepo.save(postToBeUpdated);
        }
        catch (Exception e)
        {
            throw new PostNotFoundException(POSTNOTFOUND + postId);
        }
    }

    @Override
    public String deletePost(String id) {

        try {
            Post postToBeDeleted = postRepo.findByPostId(id);
             postRepo.deleteByPostId(postToBeDeleted.getPostId());
             return DELETEPOST;
        }
        catch (Exception e)
        {
            throw new PostNotFoundException(POSTNOTFOUND + id);
        }
    }




}