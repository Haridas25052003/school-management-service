package com.demo.controller;

import com.demo.dto.ExamRequest;
import com.demo.dto.ExamResponse;
import com.demo.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    // -------------------------
    // Create Exam
    // -------------------------
    @PostMapping
    public ResponseEntity<ExamResponse> createExam(
            @PathVariable String courseId,
            @Valid @RequestBody ExamRequest request) {

        ExamResponse response =
                examService.createExam(courseId, request);

        return ResponseEntity.status(201).body(response);
    }

    // -------------------------
    // Get Exam
    // -------------------------
    @GetMapping("/{examId}")
    public ResponseEntity<ExamResponse> getExam(
            @PathVariable String courseId,
            @PathVariable String examId) {

        ExamResponse response =
                examService.getExam(courseId, examId);

        return ResponseEntity.ok(response);
    }

    // -------------------------
    // List Exams Of Course
    // -------------------------
    @GetMapping
    public ResponseEntity<List<ExamResponse>> getExams(
            @PathVariable String courseId) {

        List<ExamResponse> responses =
                examService.getExamsByCourse(courseId);

        return ResponseEntity.ok(responses);
    }

    // -------------------------
    // Delete Exam
    // -------------------------
    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(
            @PathVariable String courseId,
            @PathVariable String examId) {

        examService.deleteExam(courseId, examId);

        return ResponseEntity.noContent().build();
    }
    
    //get all exams
    @RestController
    @RequestMapping("/exams")
    public class GlobalExamController {

        private final ExamService examService;

        public GlobalExamController(ExamService examService) {
            this.examService = examService;
        }

        @GetMapping
        public ResponseEntity<List<ExamResponse>> getAllExams() {
            return ResponseEntity.ok(examService.getAllExams());
        }
    }
}