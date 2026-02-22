package com.demo.service;

import com.demo.dao.*;
import com.demo.dto.EnrollmentRequest;
import com.demo.entity.*;
import com.demo.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentDao enrollmentDao;

    @Mock
    private StudentDao studentDao;

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private String studentId;
    private String courseId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID().toString();
        courseId = UUID.randomUUID().toString();
    }

    // -------------------------
    // ✅ Success Case
    // -------------------------
    @Test
    void shouldEnrollStudentSuccessfully() {

        EnrollmentRequest request = new EnrollmentRequest();
        request.setStudentId(studentId);
        request.setCourseId(courseId);

        StudentEntity student = new StudentEntity();
        student.setStudentId(studentId);

        CourseEntity course = new CourseEntity();
        course.setCourseId(courseId);

        when(studentDao.getStudent(studentId)).thenReturn(student);
        when(courseDao.getCourse(courseId)).thenReturn(course);
        when(enrollmentDao.existsEnrollment(studentId, courseId))
                .thenReturn(false);

        enrollmentService.enrollStudent(request);

        verify(enrollmentDao).enroll(any(), any());
    }

    // -------------------------
    // ❌ Student Not Found
    // -------------------------
    @Test
    void shouldThrowStudentNotFound() {

        EnrollmentRequest request = new EnrollmentRequest();
        request.setStudentId(studentId);
        request.setCourseId(courseId);

        when(studentDao.getStudent(studentId)).thenReturn(null);

        assertThrows(StudentNotFoundException.class,
                () -> enrollmentService.enrollStudent(request));
    }

    // -------------------------
    // ❌ Course Not Found
    // -------------------------
    @Test
    void shouldThrowCourseNotFound() {

        EnrollmentRequest request = new EnrollmentRequest();
        request.setStudentId(studentId);
        request.setCourseId(courseId);

        StudentEntity student = new StudentEntity();
        student.setStudentId(studentId);

        when(studentDao.getStudent(studentId)).thenReturn(student);
        when(courseDao.getCourse(courseId)).thenReturn(null);

        assertThrows(CourseNotFoundException.class,
                () -> enrollmentService.enrollStudent(request));
    }

    // -------------------------
    // ❌ Already Enrolled
    // -------------------------
    @Test
    void shouldThrowEnrollmentAlreadyExists() {

        EnrollmentRequest request = new EnrollmentRequest();
        request.setStudentId(studentId);
        request.setCourseId(courseId);

        StudentEntity student = new StudentEntity();
        student.setStudentId(studentId);

        CourseEntity course = new CourseEntity();
        course.setCourseId(courseId);

        when(studentDao.getStudent(studentId)).thenReturn(student);
        when(courseDao.getCourse(courseId)).thenReturn(course);
        when(enrollmentDao.existsEnrollment(studentId, courseId))
                .thenReturn(true);

        assertThrows(EnrollmentAlreadyExistsException.class,
                () -> enrollmentService.enrollStudent(request));
    }
}