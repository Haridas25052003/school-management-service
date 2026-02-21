package com.demo.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class CourseEntity {

    private String pk;
    private String sk;

    private String courseId;
    private String title;
    private String description;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() { return pk; }
    public void setPk(String pk) { this.pk = pk; }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() { return sk; }
    public void setSk(String sk) { this.sk = sk; }

    @DynamoDbAttribute("courseId")
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    @DynamoDbAttribute("title")
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @DynamoDbAttribute("description")
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}