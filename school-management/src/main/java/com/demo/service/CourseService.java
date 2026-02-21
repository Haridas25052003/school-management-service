package com.demo.service;

import com.demo.dao.CourseDao;
import com.demo.dto.CourseRequest;
import com.demo.dto.CourseResponse;
import com.demo.entity.CourseEntity;
import com.demo.exception.CourseNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private static final Logger logger =
            LoggerFactory.getLogger(CourseService.class);

    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    // -------------------------
    // Create Course
    // -------------------------
    public CourseResponse createCourse(CourseRequest request) {

        logger.info("Creating course with title: {}", request.getTitle());

        String courseId = UUID.randomUUID().toString();

        CourseEntity course = new CourseEntity();
        course.setPk("COURSE#" + courseId);
        course.setSk("METADATA");
        course.setCourseId(courseId);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());

        courseDao.createCourse(course);

        logger.info("Course created successfully with id: {}", courseId);

        return new CourseResponse(
                courseId,
                request.getTitle(),
                request.getDescription()
        );
    }

    // -------------------------
    // Get Course By ID
    // -------------------------
    public CourseResponse getCourseById(String courseId) {

        logger.info("Fetching course with id: {}", courseId);

        CourseEntity course = courseDao.getCourse(courseId);

        if (course == null) {
            logger.warn("Course not found with id: {}", courseId);
            throw new CourseNotFoundException(courseId);
        }

        return new CourseResponse(
                course.getCourseId(),
                course.getTitle(),
                course.getDescription()
        );
    }

    // -------------------------
    // Get All Courses
    // -------------------------
    public List<CourseResponse> getAllCourses() {

        logger.info("Fetching all courses");

        List<CourseEntity> entities = courseDao.getAllCourses();
        List<CourseResponse> responses = new ArrayList<>();

        for (CourseEntity course : entities) {
            responses.add(
                    new CourseResponse(
                            course.getCourseId(),
                            course.getTitle(),
                            course.getDescription()
                    )
            );
        }

        logger.info("Total courses fetched: {}", responses.size());

        return responses;
    }

    // -------------------------
    // Delete Course
    // -------------------------
    public void deleteCourse(String courseId) {

        logger.info("Deleting course with id: {}", courseId);

        CourseEntity course = courseDao.getCourse(courseId);

        if (course == null) {
            logger.warn("Course not found for deletion with id: {}", courseId);
            throw new CourseNotFoundException(courseId);
        }

        courseDao.deleteCourse(course);

        logger.info("Course deleted successfully with id: {}", courseId);
    }
}