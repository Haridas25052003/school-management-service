package com.demo.integration;

import com.demo.dto.CourseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CourseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // -----------------------------------
    // ✅ Create Course
    // -----------------------------------
    @Test
    void shouldCreateCourseSuccessfully() throws Exception {

        CourseRequest request = new CourseRequest();
        request.setTitle("Integration Course " + UUID.randomUUID());
        request.setDescription("Integration Description");

        mockMvc.perform(
                post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    // -----------------------------------
    // ✅ Get All Courses
    // -----------------------------------
    @Test
    void shouldReturnAllCourses() throws Exception {

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // -----------------------------------
    // ✅ Get Course By ID
    // -----------------------------------
    @Test
    void shouldReturnCourseById() throws Exception {

        // First create course
        CourseRequest request = new CourseRequest();
        request.setTitle("Get Test " + UUID.randomUUID());
        request.setDescription("Description");

        String response = mockMvc.perform(
                post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract courseId manually (simple way)
        String courseId = objectMapper.readTree(response).get("courseId").asText();

        // Now fetch by ID
        mockMvc.perform(get("/courses/" + courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(courseId));
    }

    // -----------------------------------
    // ❌ Course Not Found
    // -----------------------------------
    @Test
    void shouldReturn404WhenCourseNotFound() throws Exception {

        mockMvc.perform(get("/courses/invalid-id"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------------
    // ✅ Delete Course
    // -----------------------------------
    @Test
    void shouldDeleteCourseSuccessfully() throws Exception {

        CourseRequest request = new CourseRequest();
        request.setTitle("Delete Test " + UUID.randomUUID());
        request.setDescription("Delete Description");

        String response = mockMvc.perform(
                post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String courseId = objectMapper.readTree(response).get("courseId").asText();

        mockMvc.perform(delete("/courses/" + courseId))
                .andExpect(status().isOk());
    }
}