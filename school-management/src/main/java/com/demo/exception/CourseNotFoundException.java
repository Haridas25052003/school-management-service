package com.demo.exception;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String courseId) {
        super("Course not found with id: " + courseId);
    }
}