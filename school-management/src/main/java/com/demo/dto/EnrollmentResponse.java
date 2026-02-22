package com.demo.dto;

public class EnrollmentResponse {

    private final String studentId;
    private final String courseId;
    private final Long enrollmentTimestamp;

    public EnrollmentResponse(String studentId,
                              String courseId,
                              Long enrollmentTimestamp) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentTimestamp = enrollmentTimestamp;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Long getEnrollmentTimestamp() {
        return enrollmentTimestamp;
    }
}