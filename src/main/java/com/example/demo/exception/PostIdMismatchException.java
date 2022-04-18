package com.example.demo.exception;

public class PostIdMismatchException  extends RuntimeException{
    public PostIdMismatchException(String s) {
        super(s);
    }
}
