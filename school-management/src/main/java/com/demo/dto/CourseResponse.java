package com.demo.dto;

public class CourseResponse {

    private String courseId;
    private String title;
    private String description;

    public CourseResponse(String courseId, String title, String description) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}