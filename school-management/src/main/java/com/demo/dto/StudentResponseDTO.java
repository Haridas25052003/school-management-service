package com.demo.dto;

public class StudentResponseDTO {
    private String studentId;
    private String name;
    private String email;
    private Integer age;

    // Default constructor (required for some frameworks)
    public StudentResponseDTO() {
    }

    // Parameterized constructor for easy object creation
    public StudentResponseDTO(String studentId, String name, String email, Integer age) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // Getters - for READING data when sending response
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

    // Setters - for WRITING data when constructing response
    // Though not always needed if we use constructor, included for flexibility
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // Optional: Override toString() for logging
    @Override
    public String toString() {
        return "StudentResponseDTO{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}