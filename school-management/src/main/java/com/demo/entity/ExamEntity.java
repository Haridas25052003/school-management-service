package com.demo.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class ExamEntity {

    private String pk;
    private String sk;

    private String examId;
    private String courseId;
    private String title;
    private Integer totalMarks;
    private Long examDate;   // timestamp

    // -------------------------
    // Primary Keys
    // -------------------------

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    // -------------------------
    // Business Fields
    // -------------------------

    @DynamoDbAttribute("examId")
    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    @DynamoDbAttribute("courseId")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @DynamoDbAttribute("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDbAttribute("totalMarks")
    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    @DynamoDbAttribute("examDate")
    public Long getExamDate() {
        return examDate;
    }

    public void setExamDate(Long examDate) {
        this.examDate = examDate;
    }
}