package com.demo.dto;

public class ExamResultResponse {

    private final String studentId;
    private final String examId;
    private final String courseId;
    private final Integer marksObtained;
    private final Long resultDate;

    public ExamResultResponse(String studentId,
                              String examId,
                              String courseId,
                              Integer marksObtained,
                              Long resultDate) {
        this.studentId = studentId;
        this.examId = examId;
        this.courseId = courseId;
        this.marksObtained = marksObtained;
        this.resultDate = resultDate;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getExamId() {
        return examId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Integer getMarksObtained() {
        return marksObtained;
    }

    public Long getResultDate() {
        return resultDate;
    }
}