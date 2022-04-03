package com.example.demo.service;
import com.example.demo.dto.PostDto;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public PostDto createPost(PostResponse postResponse) {
        Post post = new Post();
        post.setPost(postResponse.getPost());
        post.setPostedBy(postResponse.getPostedBy());
        post.setCreatedAt(LocalDate.now());
        post.setUpdatedAt(LocalDate.now());
        postRepo.save(post);
        return new PostDto(post.getId(),post.getPost(),userFeign.getUserById(post.getPostedBy()),
                post.getCreatedAt(),post.getUpdatedAt(),
                likeFeign.getLikesCount(post.getId())
                ,commentFeign.getCommentsCount(post.getId()));


    }
    @Override
    public PostResponse getPostDetails(String postId) {

        try{
            Post post = postRepo.findById(postId).get();
            PostResponse newPostResponse = new PostResponse();
            // post.setLikesCount(likeFeign.getLikesCount(postId).getBody());
            newPostResponse.setCommentsCount(commentFeign.getCommentsCount((postId))); //get the comment count
            newPostResponse.setLikesCount(likeFeign.getLikesCount(postId));
            newPostResponse.setId(post.getId());
            newPostResponse.setPostedBy(userFeign.getUserById(postId));
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
   /* @Override
    public PostDto getPostDetails(String postId) {

        Optional<Post> post1 = postRepo.findById(postId);
        if(post1.isPresent()) {
            Post post = post1.get();

            return new PostDto(post.getId(), post.getPost(), userFeign.getUserById(post.getPostedBy()), post.getCreatedAt(),
                    post.getUpdatedAt(), likeFeign.getLikesCount(postId),
                    commentFeign.getCommentsCount(postId));

        }
        else{
            throw new PostNotFoundException(POSTNOTFOUND + postId);
        }
    }
*/
    //    @Override
//    public Post updatePost(String id, Post post) {
//        return postRepo.save(new Post(post.getPostId(),post.getPost(),post.getPostedBy(),post.getCreatedAt(),post.getUpdatedAt()));
//    }
    @Override
    public PostDto updatePost(String postId, PostResponse postResponse) {
        Optional<Post> post = postRepo.findById(postId);
        if(post.isPresent()) {
            Post post1 = post.get();
            post1.setPost(postResponse.getPost());
            post1.setUpdatedAt(LocalDate.now());
            postRepo.save(post1);
            return new PostDto(post1.getId(), post1.getPost(), userFeign.getUserById(post1.getPostedBy()), post1.getCreatedAt(),
                    post1.getUpdatedAt(), likeFeign.getLikesCount(postId),
                    commentFeign.getCommentsCount(postId));
        }
        else {
            throw new PostNotFoundException(POSTNOTFOUND + postId);
        }
    }

    @Override
    public String deletePost(String postId) {

        /*try {
            Post postToBeDeleted = postRepo.findByPostId(postId);
             postRepo.deleteByPostId(postToBeDeleted.getId());
             return DELETEPOST;
        }
        catch (Exception e)
        {
            throw new PostNotFoundException(POSTNOTFOUND + postId);
        }*/
        try {
            postRepo.deleteById(postId);
            return DELETEPOST;
        }
        catch (Exception e){
            throw new PostNotFoundException(POSTNOTFOUND + postId);
        }

    }




}