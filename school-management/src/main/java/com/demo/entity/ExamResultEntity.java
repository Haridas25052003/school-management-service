package com.demo.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class ExamResultEntity {

    private String pk;
    private String sk;

    private String studentId;
    private String examId;
    private String courseId;

    private Integer marksObtained;
    private Long resultDate;

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

    @DynamoDbAttribute("studentId")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

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

    @DynamoDbAttribute("marksObtained")
    public Integer getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Integer marksObtained) {
        this.marksObtained = marksObtained;
    }

    @DynamoDbAttribute("resultDate")
    public Long getResultDate() {
        return resultDate;
    }

    public void setResultDate(Long resultDate) {
        this.resultDate = resultDate;
    }
}