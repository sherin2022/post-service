package com.example.demo.service;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.feign.CommentFeign;
import com.example.demo.feign.LikeFeign;
import com.example.demo.model.Post;
import com.example.demo.dto.PostResponse;
import com.example.demo.dto.PostRequest;
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
    public List<PostResponse> getPosts(Integer page, Integer size) {

        page = Optional.ofNullable(page).orElse(0);
        size = Optional.ofNullable(size).orElse(10);
        Pageable paging = PageRequest.of(page, size);
        Page<Post> allPosts = postRepo.findAll(paging);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : allPosts) {
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
    public PostResponse createPost(PostRequest postRequest) {

        Post newPost = postRepo.save(new Post(postRequest.getPostId(), postRequest.getPost(), postRequest.getPostedBy(),new Date(),new Date()));
        PostResponse postResponse = new PostResponse();
        postResponse.setPost(postRequest.getPost()); //setting the post
        postResponse.setCommentsCount(0); //no. of counts
        postResponse.setLikesCount(0);     //no. of likes
        postResponse.setCreatedAt(newPost.getCreatedAt());    //date created
        postResponse.setUpdatedAt(newPost.getUpdatedAt());    // date updated
        postResponse.setPostId(newPost.getPostId());
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
    public PostResponse updatePost(String postId, PostRequest postRequest) {
        Post postToBeUpdated;
        try {
            postToBeUpdated = postRepo.findByPostId(postId);
            postToBeUpdated.setPost(postRequest.getPost());
            postToBeUpdated.setUpdatedAt(new Date());
            postRepo.save(postToBeUpdated);
        } catch (Exception e) {
            throw new PostNotFoundException(POSTNOTFOUND + postId);
        }
        PostResponse updatedPostResponse = new PostResponse();
        updatedPostResponse.setCommentsCount(commentFeign.getCommentsCount((postToBeUpdated.getPostId()))); //get the comment count
        updatedPostResponse.setLikesCount(likeFeign.getLikesCount(postToBeUpdated.getPostId()));
        updatedPostResponse.setPostId(postToBeUpdated.getPostId());
        updatedPostResponse.setCreatedAt(postToBeUpdated.getCreatedAt());
        updatedPostResponse.setUpdatedAt(postToBeUpdated.getUpdatedAt());
        updatedPostResponse.setPost(postToBeUpdated.getPost());
        UserDto postToBeUpdatededByUser = userFeign.getUserDetails(postToBeUpdated.getPostedBy());
        updatedPostResponse.setPostedByUser(postToBeUpdatededByUser);

        return updatedPostResponse;
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

