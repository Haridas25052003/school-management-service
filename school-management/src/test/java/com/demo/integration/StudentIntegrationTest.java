package com.demo.integration;

import com.demo.dto.StudentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // -----------------------------------
    // ✅ Create Student Success
    // -----------------------------------
    @Test
    void shouldCreateStudentSuccessfully() throws Exception {

        StudentRequest request = new StudentRequest();
        request.setName("Integration Test");
        request.setEmail("integration" + UUID.randomUUID() + "@test.com");
        request.setAge(22);

        mockMvc.perform(
                post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Test"))
                .andExpect(jsonPath("$.email").exists());
    }

    // -----------------------------------
    // ❌ Validation Failure
    // -----------------------------------
    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {

        StudentRequest request = new StudentRequest();
        request.setName("Integration Test");
        request.setEmail("invalid-email"); // invalid format
        request.setAge(null); // assuming @NotNull

        mockMvc.perform(
                post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }
    
 // -----------------------------------
 // ✅ Get All Students
 // -----------------------------------
 @Test
 void shouldReturnAllStudents() throws Exception {

     // First create a student
     StudentRequest request = new StudentRequest();
     request.setName("List Test");
     request.setEmail("list" + UUID.randomUUID() + "@test.com");
     request.setAge(25);

     mockMvc.perform(
             post("/students")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(objectMapper.writeValueAsString(request))
     ).andExpect(status().isCreated());

     // Then fetch all students
     mockMvc.perform(
             get("/students")
     )
             .andExpect(status().isOk())
             .andExpect(jsonPath("$").isArray());
 }
}