package com.demo.dao;

import com.demo.entity.CourseEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseDao {

    private static final String TABLE_NAME = "school_management";

    private final DynamoDbTable<CourseEntity> courseTable;

    public CourseDao(DynamoDbEnhancedClient enhancedClient) {
        this.courseTable = enhancedClient.table(
                TABLE_NAME,
                TableSchema.fromBean(CourseEntity.class)
        );
    }

    // -------------------------
    // Create Course
    // -------------------------
    public void createCourse(CourseEntity course) {
        courseTable.putItem(course);
    }

    // -------------------------
    // Get Course By ID
    // -------------------------
    public CourseEntity getCourse(String courseId) {

        Key key = Key.builder()
                .partitionValue("COURSE#" + courseId)
                .sortValue("METADATA")
                .build();

        return courseTable.getItem(r -> r.key(key));
    }

    // -------------------------
    // Get All Courses
    // -------------------------
    public List<CourseEntity> getAllCourses() {

        List<CourseEntity> courses = new ArrayList<>();

        PageIterable<CourseEntity> pages = courseTable.scan();

        pages.items().forEach(item -> {
            if (item.getPk().startsWith("COURSE#")
                    && "METADATA".equals(item.getSk())) {
                courses.add(item);
            }
        });

        return courses;
    }

    // -------------------------
    // Delete Course
    // -------------------------
    public void deleteCourse(CourseEntity course) {
        courseTable.deleteItem(course);
    }
}