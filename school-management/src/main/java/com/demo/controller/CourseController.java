package com.demo.controller;

import com.demo.dto.CourseRequest;
import com.demo.dto.CourseResponse;
import com.demo.dto.StudentResponse;
import com.demo.service.CourseService;
import com.demo.service.EnrollmentService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public CourseController(CourseService courseService,EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService=enrollmentService;
    }

    // -------------------------
    // Create Course
    // -------------------------
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response = courseService.createCourse(request);

        return ResponseEntity.status(201).body(response);
    }

    // -------------------------
    // Get Course By ID
    // -------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourse(
            @PathVariable String id) {

        CourseResponse response = courseService.getCourseById(id);

        return ResponseEntity.ok(response);
    }

    // -------------------------
    // Get All Courses
    // -------------------------
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {

        List<CourseResponse> courses = courseService.getAllCourses();

        return ResponseEntity.ok(courses);
    }

    // -------------------------
    // Delete Course
    // -------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable String id) {

        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsOfCourse(
            @PathVariable String id) {

        List<StudentResponse> students =
                enrollmentService.getStudentsByCourseId(id);

        return ResponseEntity.ok(students);
    }
}