package com.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ExamRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Total marks is required")
    @Positive(message = "Total marks must be positive")
    private Integer totalMarks;

    @NotNull(message = "Exam date is required")
    private Long examDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Long getExamDate() {
        return examDate;
    }

    public void setExamDate(Long examDate) {
        this.examDate = examDate;
    }
}