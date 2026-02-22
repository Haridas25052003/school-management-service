package com.demo.exception;

public class StudentNotEnrolledException extends RuntimeException {

    public StudentNotEnrolledException(String studentId, String courseId) {
        super("Student " + studentId +
              " is not enrolled in course " + courseId);
    }
}