package com.demo.service;

import com.demo.dao.CourseDao;
import com.demo.dto.CourseRequest;
import com.demo.dto.CourseResponse;
import com.demo.entity.CourseEntity;
import com.demo.exception.CourseNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private CourseService courseService;

    private String courseId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID().toString();
    }

    // -------------------------
    // ✅ Create Course Success
    // -------------------------
    @Test
    void shouldCreateCourseSuccessfully() {

        CourseRequest request = new CourseRequest();
        request.setTitle("Java");
        request.setDescription("Core Java");

        CourseResponse response =
                courseService.createCourse(request);

        assertNotNull(response);
        assertEquals("Java", response.getTitle());
        assertEquals("Core Java", response.getDescription());

        verify(courseDao).createCourse(any(CourseEntity.class));
    }

    // -------------------------
    // ✅ Get Course By ID Success
    // -------------------------
    @Test
    void shouldReturnCourseWhenFound() {

        CourseEntity entity = new CourseEntity();
        entity.setCourseId(courseId);
        entity.setTitle("Java");
        entity.setDescription("Core Java");

        when(courseDao.getCourse(courseId))
                .thenReturn(entity);

        CourseResponse response =
                courseService.getCourseById(courseId);

        assertEquals(courseId, response.getCourseId());
        assertEquals("Java", response.getTitle());
    }

    // -------------------------
    // ❌ Get Course Not Found
    // -------------------------
    @Test
    void shouldThrowWhenCourseNotFound() {

        when(courseDao.getCourse(courseId))
                .thenReturn(null);

        assertThrows(CourseNotFoundException.class,
                () -> courseService.getCourseById(courseId));
    }

    // -------------------------
    // ✅ Get All Courses
    // -------------------------
    @Test
    void shouldReturnAllCourses() {

        CourseEntity entity = new CourseEntity();
        entity.setCourseId(courseId);
        entity.setTitle("Java");
        entity.setDescription("Core Java");

        when(courseDao.getAllCourses())
                .thenReturn(List.of(entity));

        List<CourseResponse> responses =
                courseService.getAllCourses();

        assertEquals(1, responses.size());
        assertEquals("Java", responses.get(0).getTitle());
    }

    // -------------------------
    // ❌ Delete Course Not Found
    // -------------------------
    @Test
    void shouldThrowWhenDeletingNonExistingCourse() {

        when(courseDao.getCourse(courseId))
                .thenReturn(null);

        assertThrows(CourseNotFoundException.class,
                () -> courseService.deleteCourse(courseId));
    }

    // -------------------------
    // ✅ Delete Course Success
    // -------------------------
    @Test
    void shouldDeleteCourseSuccessfully() {

        CourseEntity entity = new CourseEntity();
        entity.setCourseId(courseId);

        when(courseDao.getCourse(courseId))
                .thenReturn(entity);

        courseService.deleteCourse(courseId);

        verify(courseDao).deleteCourse(entity);
    }
}