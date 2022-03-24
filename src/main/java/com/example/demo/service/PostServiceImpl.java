package com.example.demo.service;

import com.example.demo.dto.User;
import com.example.demo.feign.CommentFeign;
import com.example.demo.feign.LikeFeign;
import com.example.demo.dto.Post;
import com.example.demo.dto.PostResponse;
import com.example.demo.feign.UserFeign;
import com.example.demo.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

//    @Override
//    public Post updatePost(String id, Post post) {
//        return postRepo.save(new Post(post.getPostId(),post.getPost(),post.getPostedBy(),post.getCreatedAt(),post.getUpdatedAt()));
//    }
    @Override
    public Post updatePost(String postId, Post post) {
        Post postToBeUpdated = postRepo.findByPostId(postId);
        postToBeUpdated.setPost(post.getPost());
        //postToBeUpdated.setPostedBy(post.getPostedBy());
        postToBeUpdated.setUpdatedAt(new Date());
        return postRepo.save(postToBeUpdated);
}

    @Override
    public Post deletePost(String id) {

        Post postToBeDeleted = postRepo.findByPostId(id);
        return postRepo.deleteByPostId(postToBeDeleted.getPostId());
    }




}
