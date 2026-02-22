package com.demo.exception;

public class ExamNotFoundException extends RuntimeException {

    public ExamNotFoundException(String examId) {
        super("Exam not found with id: " + examId);
    }
}