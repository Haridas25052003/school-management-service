package com.demo.service;

import com.demo.dao.CourseDao;
import com.demo.dao.ExamDao;
import com.demo.dto.ExamRequest;
import com.demo.dto.ExamResponse;
import com.demo.entity.CourseEntity;
import com.demo.entity.ExamEntity;
import com.demo.exception.CourseNotFoundException;
import com.demo.exception.ExamNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ExamServiceTest {

    @Mock
    private ExamDao examDao;

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private ExamService examService;

    private String courseId;
    private String examId;

    @BeforeEach
    void setup() {
        courseId = UUID.randomUUID().toString();
        examId = UUID.randomUUID().toString();
    }

    // ✅ Create Exam Success
    @Test
    void shouldCreateExamSuccessfully() {

        ExamRequest request = new ExamRequest();
        request.setTitle("Midterm");
        request.setTotalMarks(100);
        request.setExamDate(1710000000000L);

        CourseEntity course = new CourseEntity();
        course.setCourseId(courseId);

        when(courseDao.getCourse(courseId)).thenReturn(course);

        ExamResponse response =
                examService.createExam(courseId, request);

        assertNotNull(response);
        assertEquals("Midterm", response.getTitle());

        verify(examDao).createExam(any(ExamEntity.class));
    }

    // ❌ Create Exam - Course Not Found
    @Test
    void shouldThrowWhenCreatingExamIfCourseNotFound() {

        when(courseDao.getCourse(courseId)).thenReturn(null);

        ExamRequest request = new ExamRequest();

        assertThrows(CourseNotFoundException.class,
                () -> examService.createExam(courseId, request));
    }

    // ✅ Get Exam Success
    @Test
    void shouldReturnExamWhenFound() {

        ExamEntity entity = new ExamEntity();
        entity.setExamId(examId);
        entity.setCourseId(courseId);
        entity.setTitle("Midterm");
        entity.setTotalMarks(100);
        entity.setExamDate(1710000000000L);

        when(examDao.getExam(courseId, examId))
                .thenReturn(entity);

        ExamResponse response =
                examService.getExam(courseId, examId);

        assertEquals("Midterm", response.getTitle());
    }

    // ❌ Get Exam Not Found
    @Test
    void shouldThrowWhenExamNotFound() {

        when(examDao.getExam(courseId, examId))
                .thenReturn(null);

        assertThrows(ExamNotFoundException.class,
                () -> examService.getExam(courseId, examId));
    }

    // ✅ Get Exams By Course Success
    @Test
    void shouldReturnExamsByCourse() {

        CourseEntity course = new CourseEntity();
        course.setCourseId(courseId);

        ExamEntity entity = new ExamEntity();
        entity.setExamId(examId);
        entity.setCourseId(courseId);
        entity.setTitle("Midterm");

        when(courseDao.getCourse(courseId)).thenReturn(course);
        when(examDao.findByCourseId(courseId))
                .thenReturn(List.of(entity));

        List<ExamResponse> responses =
                examService.getExamsByCourse(courseId);

        assertEquals(1, responses.size());
    }

    // ❌ Get Exams By Course - Course Not Found
    @Test
    void shouldThrowWhenGettingExamsIfCourseNotFound() {

        when(courseDao.getCourse(courseId)).thenReturn(null);

        assertThrows(CourseNotFoundException.class,
                () -> examService.getExamsByCourse(courseId));
    }

    // ✅ Delete Exam Success
    @Test
    void shouldDeleteExamSuccessfully() {

        ExamEntity entity = new ExamEntity();
        entity.setExamId(examId);
        entity.setCourseId(courseId);

        when(examDao.getExam(courseId, examId))
                .thenReturn(entity);

        examService.deleteExam(courseId, examId);

        verify(examDao).deleteExam(entity);
    }

    // ❌ Delete Exam Not Found
    @Test
    void shouldThrowWhenDeletingNonExistingExam() {

        when(examDao.getExam(courseId, examId))
                .thenReturn(null);

        assertThrows(ExamNotFoundException.class,
                () -> examService.deleteExam(courseId, examId));
    }

    // ✅ Get All Exams
    @Test
    void shouldReturnAllExams() {

        ExamEntity entity = new ExamEntity();
        entity.setExamId(examId);
        entity.setCourseId(courseId);
        entity.setTitle("Midterm");

        when(examDao.findAllExams())
                .thenReturn(List.of(entity));

        List<ExamResponse> responses =
                examService.getAllExams();

        assertEquals(1, responses.size());
    }
}