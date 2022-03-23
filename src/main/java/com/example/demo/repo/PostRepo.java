package com.example.demo.repo;
import com.example.demo.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepo extends MongoRepository<Post, String> {
    Post findByPostId(String id);
    Post deleteByPostId(String id);
}