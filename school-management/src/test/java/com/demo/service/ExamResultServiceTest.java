package com.demo.service;

import com.demo.dao.*;
import com.demo.dto.ExamResultRequest;
import com.demo.dto.ExamResultResponse;
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
class ExamResultServiceTest {

    @Mock
    private ExamResultDao examResultDao;

    @Mock
    private StudentDao studentDao;

    @Mock
    private ExamDao examDao;

    @Mock
    private EnrollmentDao enrollmentDao;

    @InjectMocks
    private ExamResultService examResultService;

    private String studentId;
    private String examId;
    private String courseId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID().toString();
        examId = UUID.randomUUID().toString();
        courseId = UUID.randomUUID().toString();
    }

    // -------------------------
    // ✅ Success Case
    // -------------------------
    @Test
    void shouldCreateResultSuccessfully() {

        ExamResultRequest request = new ExamResultRequest();
        request.setStudentId(studentId);
        request.setExamId(examId);
        request.setMarksObtained(85);

        StudentEntity student = new StudentEntity();
        student.setStudentId(studentId);

        ExamEntity exam = new ExamEntity();
        exam.setExamId(examId);
        exam.setCourseId(courseId);

        when(studentDao.getStudent(studentId)).thenReturn(student);
        when(examDao.findAllExams()).thenReturn(java.util.List.of(exam));
        when(enrollmentDao.existsEnrollment(studentId, courseId)).thenReturn(true);

        ExamResultResponse response =
                examResultService.createResult(request);

        assertNotNull(response);
        assertEquals(studentId, response.getStudentId());
        assertEquals(examId, response.getExamId());
        assertEquals(85, response.getMarksObtained());

        verify(examResultDao, times(1))
                .createResult(any(), any());
    }

    // -------------------------
    // ❌ Student Not Found
    // -------------------------
    @Test
    void shouldThrowStudentNotFound() {

        ExamResultRequest request = new ExamResultRequest();
        request.setStudentId(studentId);
        request.setExamId(examId);
        request.setMarksObtained(80);

        when(studentDao.getStudent(studentId)).thenReturn(null);

        assertThrows(StudentNotFoundException.class,
                () -> examResultService.createResult(request));
    }

    // -------------------------
    // ❌ Exam Not Found
    // -------------------------
    @Test
    void shouldThrowExamNotFound() {

        ExamResultRequest request = new ExamResultRequest();
        request.setStudentId(studentId);
        request.setExamId(examId);
        request.setMarksObtained(75);

        StudentEntity student = new StudentEntity();
        student.setStudentId(studentId);

        when(studentDao.getStudent(studentId)).thenReturn(student);
        when(examDao.findAllExams()).thenReturn(java.util.List.of());

        assertThrows(ExamNotFoundException.class,
                () -> examResultService.createResult(request));
    }

    // -------------------------
    // ❌ Student Not Enrolled
    // -------------------------
    @Test
    void shouldThrowStudentNotEnrolled() {

        ExamResultRequest request = new ExamResultRequest();
        request.setStudentId(studentId);
        request.setExamId(examId);
        request.setMarksObtained(70);

        StudentEntity student = new StudentEntity();
        student.setStudentId(studentId);

        ExamEntity exam = new ExamEntity();
        exam.setExamId(examId);
        exam.setCourseId(courseId);

        when(studentDao.getStudent(studentId)).thenReturn(student);
        when(examDao.findAllExams()).thenReturn(java.util.List.of(exam));
        when(enrollmentDao.existsEnrollment(studentId, courseId))
                .thenReturn(false);

        assertThrows(StudentNotEnrolledException.class,
                () -> examResultService.createResult(request));
    }

    // -------------------------
    // ❌ Duplicate Result
    // -------------------------
    @Test
    void shouldThrowDuplicateResult() {

        ExamResultRequest request = new ExamResultRequest();
        request.setStudentId(studentId);
        request.setExamId(examId);
        request.setMarksObtained(90);

        StudentEntity student = new StudentEntity();
        student.setStudentId(studentId);

        ExamEntity exam = new ExamEntity();
        exam.setExamId(examId);
        exam.setCourseId(courseId);

        when(studentDao.getStudent(studentId)).thenReturn(student);
        when(examDao.findAllExams()).thenReturn(java.util.List.of(exam));
        when(enrollmentDao.existsEnrollment(studentId, courseId))
                .thenReturn(true);

        doThrow(new RuntimeException())
                .when(examResultDao)
                .createResult(any(), any());

        assertThrows(ResultAlreadyExistsException.class,
                () -> examResultService.createResult(request));
    }
}