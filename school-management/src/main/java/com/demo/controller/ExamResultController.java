package com.demo.controller;

import com.demo.dto.ExamResultRequest;
import com.demo.dto.ExamResultResponse;
import com.demo.service.ExamResultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ExamResultController {

    private final ExamResultService examResultService;

    public ExamResultController(ExamResultService examResultService) {
        this.examResultService = examResultService;
    }

    // -------------------------
    // Create Result
    // -------------------------
    @PostMapping
    public ResponseEntity<ExamResultResponse> createResult(
            @Valid @RequestBody ExamResultRequest request) {

        ExamResultResponse response =
                examResultService.createResult(request);

        return ResponseEntity.status(201).body(response);
    }

    // -------------------------
    // Get Results of Student
    // -------------------------
    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<ExamResultResponse>> getResultsByStudent(
            @PathVariable String studentId) {

        return ResponseEntity.ok(
                examResultService.getResultsByStudent(studentId)
        );
    }

    // -------------------------
    // Get Results of Exam
    // -------------------------
    @GetMapping("/exams/{examId}")
    public ResponseEntity<List<ExamResultResponse>> getResultsByExam(
            @PathVariable String examId) {

        return ResponseEntity.ok(
                examResultService.getResultsByExam(examId)
        );
    }
}