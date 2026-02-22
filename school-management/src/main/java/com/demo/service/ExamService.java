package com.demo.service;

import com.demo.dao.CourseDao;
import com.demo.dao.ExamDao;
import com.demo.dto.ExamRequest;
import com.demo.dto.ExamResponse;
import com.demo.entity.CourseEntity;
import com.demo.entity.ExamEntity;
import com.demo.exception.CourseNotFoundException;
import com.demo.exception.ExamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExamService {

    private static final Logger logger =
            LoggerFactory.getLogger(ExamService.class);

    private final ExamDao examDao;
    private final CourseDao courseDao;

    public ExamService(ExamDao examDao, CourseDao courseDao) {
        this.examDao = examDao;
        this.courseDao = courseDao;
    }

    // -------------------------
    // Create Exam
    // -------------------------
    public ExamResponse createExam(String courseId, ExamRequest request) {

        logger.info("Creating exam for course {}", courseId);

        // 1️⃣ Validate course exists
        CourseEntity course = courseDao.getCourse(courseId);

        if (course == null) {
            logger.warn("Course not found: {}", courseId);
            throw new CourseNotFoundException(courseId);
        }

        // 2️⃣ Generate examId
        String examId = UUID.randomUUID().toString();

        // 3️⃣ Build entity
        ExamEntity exam = new ExamEntity();
        exam.setPk("COURSE#" + courseId);
        exam.setSk("EXAM#" + examId);
        exam.setExamId(examId);
        exam.setCourseId(courseId);
        exam.setTitle(request.getTitle());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setExamDate(request.getExamDate());

        examDao.createExam(exam);

        logger.info("Exam created with id {}", examId);

        return new ExamResponse(
                examId,
                courseId,
                request.getTitle(),
                request.getTotalMarks(),
                request.getExamDate()
        );
    }
    // -------------------------
    // Get Exam
    // -------------------------
    public ExamResponse getExam(String courseId, String examId) {

        logger.info("Fetching exam {} for course {}", examId, courseId);

        ExamEntity exam = examDao.getExam(courseId, examId);

        if (exam == null) {
            logger.warn("Exam not found: {}", examId);
            throw new ExamNotFoundException(examId);
        }

        return new ExamResponse(
                exam.getExamId(),
                exam.getCourseId(),
                exam.getTitle(),
                exam.getTotalMarks(),
                exam.getExamDate()
        );
    }

    // -------------------------
    // List Exams Of Course
    // -------------------------
    public List<ExamResponse> getExamsByCourse(String courseId) {

        logger.info("Fetching exams for course {}", courseId);

        // Validate course exists
        CourseEntity course = courseDao.getCourse(courseId);
        if (course == null) {
            logger.warn("Course not found: {}", courseId);
            throw new CourseNotFoundException(courseId);
        }

        List<ExamEntity> entities =
                examDao.findByCourseId(courseId);

        List<ExamResponse> responses = new ArrayList<>();

        for (ExamEntity exam : entities) {
            responses.add(
                    new ExamResponse(
                            exam.getExamId(),
                            exam.getCourseId(),
                            exam.getTitle(),
                            exam.getTotalMarks(),
                            exam.getExamDate()
                    )
            );
        }

        logger.info("Total exams found: {}", responses.size());

        return responses;
    }

    // -------------------------
    // Delete Exam
    // -------------------------
    public void deleteExam(String courseId, String examId) {

        logger.info("Deleting exam {} for course {}", examId, courseId);

        ExamEntity exam = examDao.getExam(courseId, examId);

        if (exam == null) {
            logger.warn("Exam not found: {}", examId);
            throw new ExamNotFoundException(examId);
        }

        examDao.deleteExam(exam);

        logger.info("Exam deleted successfully");
    }
    
    //exam service
    public List<ExamResponse> getAllExams() {

        logger.info("Fetching all exams (admin operation)");

        List<ExamEntity> entities = examDao.findAllExams();
        List<ExamResponse> responses = new ArrayList<>();

        for (ExamEntity exam : entities) {
            responses.add(
                    new ExamResponse(
                            exam.getExamId(),
                            exam.getCourseId(),
                            exam.getTitle(),
                            exam.getTotalMarks(),
                            exam.getExamDate()
                    )
            );
        }

        return responses;
    }
    
    
}