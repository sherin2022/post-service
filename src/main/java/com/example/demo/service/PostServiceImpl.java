package com.example.demo.service;
import com.example.demo.model.Post;
import com.example.demo.model.PostResponse;
import com.example.demo.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepo postRepo;


    @Override
    public List<Post> getPosts() {
        return postRepo.findAll();
    }

    @Override
    public PostResponse createPost(Post post) {

        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());

        PostResponse postResponse= new PostResponse();

        postResponse.setPostId(post.getPostId());
        postResponse.setPost(post.getPost());
        postResponse.setPostedBy(post.getPostedBy());
        postResponse.setCommentsCount(0);
        postResponse.setLikesCount(0);
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setUpdatedAt(post.getUpdatedAt());

        postRepo.save(new Post(post.getPostId(),post.getPost(),post.getPostedBy(),post.getCreatedAt(),post.getUpdatedAt()));

        return postResponse;
    }

    @Override
    public Post getPostDetails(String id) {
        return postRepo.findByPostId(id);
    }

    @Override
    public Post updatePost(String postId, Post post) {
        Post postToBeUpdated = postRepo.findByPostId(postId);
        postToBeUpdated.setPost(post.getPost());
        postToBeUpdated.setPostedBy(post.getPostedBy());
        postToBeUpdated.setCreatedAt(new Date());
        postToBeUpdated.setUpdatedAt(new Date());
        return postRepo.save(postToBeUpdated);
    }

    @Override
    public Post deletePost(String id) {

        Post postToBeDeleted = postRepo.findByPostId(id);
        return postRepo.deleteByPostId(postToBeDeleted.getPostId());
    }
}