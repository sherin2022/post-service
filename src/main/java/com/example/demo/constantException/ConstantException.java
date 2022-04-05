package com.example.demo.constantException;

public class ConstantException {
    private ConstantException() {
        // restrict instantiation
    }

    public static final String DELETEPOST = "Post deleted"  ;
    public static final String POSTNOTFOUND = "Post not found for : ";

}
