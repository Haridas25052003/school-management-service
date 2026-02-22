package com.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ExamResultRequest {

    @NotBlank(message = "StudentId is required")
    private String studentId;

    @NotBlank(message = "ExamId is required")
    private String examId;

    @NotNull(message = "Marks obtained is required")
    @Positive(message = "Marks must be positive")
    private Integer marksObtained;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public Integer getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Integer marksObtained) {
        this.marksObtained = marksObtained;
    }
}