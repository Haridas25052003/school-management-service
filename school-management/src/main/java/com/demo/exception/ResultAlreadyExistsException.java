package com.demo.exception;

public class ResultAlreadyExistsException extends RuntimeException {

    public ResultAlreadyExistsException(String studentId, String examId) {
        super("Result already exists for student " +
              studentId + " and exam " + examId);
    }
}