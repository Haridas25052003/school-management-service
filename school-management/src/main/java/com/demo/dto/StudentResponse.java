package com.demo.dto;

public class StudentResponse {

    private String studentId;
    private String name;
    private String email;
    private Integer age;

    public StudentResponse(String studentId, String name, String email, Integer age) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}