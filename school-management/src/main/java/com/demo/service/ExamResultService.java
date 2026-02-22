package com.demo.service;

import com.demo.dao.*;
import com.demo.dto.ExamResultRequest;
import com.demo.dto.ExamResultResponse;
import com.demo.entity.*;
import com.demo.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExamResultService {

    private static final Logger logger =
            LoggerFactory.getLogger(ExamResultService.class);

    private final ExamResultDao examResultDao;
    private final StudentDao studentDao;
    private final ExamDao examDao;
    private final EnrollmentDao enrollmentDao;

    public ExamResultService(ExamResultDao examResultDao,
                             StudentDao studentDao,
                             ExamDao examDao,
                             EnrollmentDao enrollmentDao) {
        this.examResultDao = examResultDao;
        this.studentDao = studentDao;
        this.examDao = examDao;
        this.enrollmentDao = enrollmentDao;
    }

    // -------------------------
    // Create Exam Result
    // -------------------------
    public ExamResultResponse createResult(ExamResultRequest request) {

        logger.info("Creating result for student {} and exam {}",
                request.getStudentId(), request.getExamId());

        // 1️⃣ Validate student exists
        StudentEntity student =
                studentDao.getStudent(request.getStudentId());

        if (student == null) {
            logger.warn("Student not found: {}", request.getStudentId());
            throw new StudentNotFoundException(request.getStudentId());
        }

        // 2️⃣ Validate exam exists
        // We need courseId, so fetch exam
        // Since exams are under course partition,
        // we must search properly (we will improve this later)

        ExamEntity exam = findExamByExamId(request.getExamId());

        if (exam == null) {
            logger.warn("Exam not found: {}", request.getExamId());
            throw new ExamNotFoundException(request.getExamId());
        }

        String courseId = exam.getCourseId();

        // 3️⃣ Validate student enrolled in course
        boolean enrolled =
                enrollmentDao.existsEnrollment(
                        request.getStudentId(), courseId);

        if (!enrolled) {
            logger.warn("Student {} not enrolled in course {}",
                    request.getStudentId(), courseId);
            throw new StudentNotEnrolledException(
                    request.getStudentId(), courseId);
        }

        // 4️⃣ Prepare resultDate
        Long resultDate = System.currentTimeMillis();

        // 5️⃣ Build student-side record
        ExamResultEntity studentSide = new ExamResultEntity();
        studentSide.setPk("STUDENT#" + request.getStudentId());
        studentSide.setSk("RESULT#" + request.getExamId());
        studentSide.setStudentId(request.getStudentId());
        studentSide.setExamId(request.getExamId());
        studentSide.setCourseId(courseId);
        studentSide.setMarksObtained(request.getMarksObtained());
        studentSide.setResultDate(resultDate);

        // 6️⃣ Build exam-side record
        ExamResultEntity examSide = new ExamResultEntity();
        examSide.setPk("EXAM#" + request.getExamId());
        examSide.setSk("STUDENT#" + request.getStudentId());
        examSide.setStudentId(request.getStudentId());
        examSide.setExamId(request.getExamId());
        examSide.setCourseId(courseId);
        examSide.setMarksObtained(request.getMarksObtained());
        examSide.setResultDate(resultDate);

        try {
            examResultDao.createResult(studentSide, examSide);
        } catch (Exception e) {
            logger.warn("Duplicate result attempt for student {} and exam {}",
                    request.getStudentId(), request.getExamId());
            throw new ResultAlreadyExistsException(
                    request.getStudentId(),
                    request.getExamId());
        }

        logger.info("Result created successfully");

        return new ExamResultResponse(
                request.getStudentId(),
                request.getExamId(),
                courseId,
                request.getMarksObtained(),
                resultDate
        );
    }

    // -------------------------
    // Temporary helper method
    // -------------------------
    private ExamEntity findExamByExamId(String examId) {

        // Since exam stored under COURSE partition,
        // and we don’t have GSI,
        // we scan to find exam.

        return examDao.findAllExams()
                .stream()
                .filter(e -> e.getExamId().equals(examId))
                .findFirst()
                .orElse(null);
    }
}