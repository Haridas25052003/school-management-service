package com.demo.controller;

import com.demo.dto.EnrollmentRequest;
import com.demo.dto.EnrollmentResponse;
import com.demo.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // -------------------------
    // Enroll Student
    // -------------------------
    @PostMapping
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @Valid @RequestBody EnrollmentRequest request) {

        EnrollmentResponse response =
                enrollmentService.enrollStudent(request);

        return ResponseEntity.status(201).body(response);
    }
}