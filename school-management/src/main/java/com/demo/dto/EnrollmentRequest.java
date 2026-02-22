package com.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class EnrollmentRequest {

    @NotBlank(message = "StudentId is required")
    private String studentId;

    @NotBlank(message = "CourseId is required")
    private String courseId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}