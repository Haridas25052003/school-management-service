package com.demo.dto;

public class ExamResponse {

    private final String examId;
    private final String courseId;
    private final String title;
    private final Integer totalMarks;
    private final Long examDate;

    public ExamResponse(String examId,
                        String courseId,
                        String title,
                        Integer totalMarks,
                        Long examDate) {
        this.examId = examId;
        this.courseId = courseId;
        this.title = title;
        this.totalMarks = totalMarks;
        this.examDate = examDate;
    }

    public String getExamId() {
        return examId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public Long getExamDate() {
        return examDate;
    }
}