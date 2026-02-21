package com.demo.service;

import com.demo.dao.StudentDao;
import com.demo.dto.StudentRequest;
import com.demo.dto.StudentResponse;
import com.demo.entity.StudentEntity;
import com.demo.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private StudentDao studentDao;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentDao = mock(StudentDao.class);
        studentService = new StudentService(studentDao);
    }

    @Test
    void createStudent_shouldReturnResponse() {

        StudentRequest request = new StudentRequest();
        request.setName("John");
        request.setEmail("john@gmail.com");
        request.setAge(22);

        StudentResponse response = studentService.createStudent(request);

        assertNotNull(response.getStudentId());
        assertEquals("John", response.getName());
        assertEquals("john@gmail.com", response.getEmail());
        assertEquals(22, response.getAge());

        verify(studentDao, times(1))
                .createStudent(any(), any());
    }

    @Test
    void getStudentById_whenNotFound_shouldThrowException() {

        when(studentDao.getStudent("123"))
                .thenReturn(null);

        assertThrows(StudentNotFoundException.class, () ->
                studentService.getStudentById("123"));
    }
}