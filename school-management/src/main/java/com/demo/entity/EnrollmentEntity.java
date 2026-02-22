package com.demo.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class EnrollmentEntity {

    private String pk;
    private String sk;

    private String studentId;
    private String courseId;

    // Used for LSI sorting (timestamp)
    private Long lsi1sk;

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
    // Business Attributes
    // -------------------------

    @DynamoDbAttribute("studentId")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @DynamoDbAttribute("courseId")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    // -------------------------
    // LSI Sort Key (timestamp)
    // -------------------------

    @DynamoDbSecondarySortKey(indexNames = "lsi1")
    @DynamoDbAttribute("LSI1SK")
    public Long getLsi1sk() {
        return lsi1sk;
    }

    public void setLsi1sk(Long lsi1sk) {
        this.lsi1sk = lsi1sk;
    }
}