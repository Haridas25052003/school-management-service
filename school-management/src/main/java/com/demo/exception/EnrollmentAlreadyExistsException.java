package com.demo.exception;

public class EnrollmentAlreadyExistsException extends RuntimeException {

    public EnrollmentAlreadyExistsException(String studentId, String courseId) {
        super("Student " + studentId + 
              " is already enrolled in course " + courseId);
    }
}