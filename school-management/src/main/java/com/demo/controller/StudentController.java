package com.demo.controller;

import com.demo.dto.StudentRequest;
import com.demo.dto.StudentResponse;
import com.demo.service.StudentService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //adding student
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.createStudent(request);

        return ResponseEntity.ok(response);
    }
    
    //get student by id
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable String id) {

        StudentResponse response = studentService.getStudentById(id);

        return ResponseEntity.ok(response);
    }
    
    //deleting student 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }
    
    //get all the student
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {

        List<StudentResponse> students =
                studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }
}