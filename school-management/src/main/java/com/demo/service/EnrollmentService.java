package com.demo.service;

import com.demo.dao.CourseDao;
import com.demo.dao.EnrollmentDao;
import com.demo.dao.StudentDao;
import com.demo.dto.EnrollmentRequest;
import com.demo.dto.EnrollmentResponse;
import com.demo.entity.CourseEntity;
import com.demo.entity.EnrollmentEntity;
import com.demo.entity.StudentEntity;
import com.demo.exception.CourseNotFoundException;
import com.demo.exception.EnrollmentAlreadyExistsException;
import com.demo.exception.StudentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

@Service
public class EnrollmentService {

    private static final Logger logger =
            LoggerFactory.getLogger(EnrollmentService.class);

    private final EnrollmentDao enrollmentDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public EnrollmentService(EnrollmentDao enrollmentDao,
                             StudentDao studentDao,
                             CourseDao courseDao) {
        this.enrollmentDao = enrollmentDao;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    // -------------------------
    // Enroll Student
    // -------------------------
    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {

        logger.info("Enrolling student {} into course {}",
                request.getStudentId(), request.getCourseId());

        // 1️⃣ Validate Student exists
        StudentEntity student =
                studentDao.getStudent(request.getStudentId());

        if (student == null) {
            logger.warn("Student not found: {}", request.getStudentId());
            throw new StudentNotFoundException(request.getStudentId());
        }

        // 2️⃣ Validate Course exists
        CourseEntity course =
                courseDao.getCourse(request.getCourseId());

        if (course == null) {
            logger.warn("Course not found: {}", request.getCourseId());
            throw new CourseNotFoundException(request.getCourseId());
        }

        // 3️⃣ Generate timestamp
        Long timestamp = System.currentTimeMillis();

        // 4️⃣ Build Student-side record
        EnrollmentEntity studentSide = new EnrollmentEntity();
        studentSide.setPk("STUDENT#" + request.getStudentId());
        studentSide.setSk("COURSE#" + request.getCourseId());
        studentSide.setStudentId(request.getStudentId());
        studentSide.setCourseId(request.getCourseId());
        studentSide.setLsi1sk(timestamp);

        // 5️⃣ Build Course-side record
        EnrollmentEntity courseSide = new EnrollmentEntity();
        courseSide.setPk("COURSE#" + request.getCourseId());
        courseSide.setSk("STUDENT#" + request.getStudentId());
        courseSide.setStudentId(request.getStudentId());
        courseSide.setCourseId(request.getCourseId());
        courseSide.setLsi1sk(timestamp);

        try {
            enrollmentDao.enroll(studentSide, courseSide);
        } catch (TransactionCanceledException e) {
            logger.warn("Duplicate enrollment detected for student {} and course {}",
                    request.getStudentId(), request.getCourseId());
            throw new EnrollmentAlreadyExistsException(
                    request.getStudentId(),
                    request.getCourseId()
            );
        }

        logger.info("Enrollment successful");

        return new EnrollmentResponse(
                request.getStudentId(),
                request.getCourseId(),
                timestamp
        );
    }
}