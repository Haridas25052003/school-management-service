package com.demo.service;

import com.demo.dao.StudentDao;
import com.demo.dto.StudentRequest;
import com.demo.dto.StudentResponse;
import com.demo.entity.EmailUniqueEntity;
import com.demo.entity.StudentEntity;
import com.demo.exception.EmailAlreadyExistsException;
import com.demo.exception.StudentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private static final Logger logger =
            LoggerFactory.getLogger(StudentService.class);

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    // -------------------------
    // Create Student
    // -------------------------
    public StudentResponse createStudent(StudentRequest request) {

        logger.info("Creating student with email: {}", request.getEmail());

        String studentId = UUID.randomUUID().toString();

        StudentEntity student = new StudentEntity();
        student.setPk("STUDENT#" + studentId);
        student.setSk("METADATA");
        student.setStudentId(studentId);
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());

        EmailUniqueEntity emailUnique = new EmailUniqueEntity();
        emailUnique.setPk("EMAIL#" + request.getEmail());
        emailUnique.setSk("METADATA");
        emailUnique.setStudentId(studentId);

        try {
            studentDao.createStudent(student, emailUnique);
        } catch (TransactionCanceledException e) {
            logger.warn("Duplicate email detected: {}", request.getEmail());
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        logger.info("Student created successfully with id: {}", studentId);

        return new StudentResponse(
                studentId,
                request.getName(),
                request.getEmail(),
                request.getAge()
        );
    }

    // -------------------------
    // Get Student By ID
    // -------------------------
    public StudentResponse getStudentById(String studentId) {

        logger.info("Fetching student with id: {}", studentId);

        StudentEntity student = studentDao.getStudent(studentId);

        if (student == null) {
            logger.warn("Student not found with id: {}", studentId);
            throw new StudentNotFoundException(studentId);
        }

        return new StudentResponse(
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getAge()
        );
    }

    // -------------------------
    // Get All Students
    // -------------------------
    public List<StudentResponse> getAllStudents() {

        logger.info("Fetching all students");

        List<StudentEntity> entities = studentDao.getAllStudents();
        List<StudentResponse> responses = new ArrayList<>();

        for (StudentEntity student : entities) {
            responses.add(
                    new StudentResponse(
                            student.getStudentId(),
                            student.getName(),
                            student.getEmail(),
                            student.getAge()
                    )
            );
        }

        logger.info("Total students fetched: {}", responses.size());

        return responses;
    }

    // -------------------------
    // Delete Student
    // -------------------------
    public void deleteStudent(String studentId) {

        logger.info("Deleting student with id: {}", studentId);

        StudentEntity student = studentDao.getStudent(studentId);

        if (student == null) {
            logger.warn("Student not found for deletion with id: {}", studentId);
            throw new StudentNotFoundException(studentId);
        }

        EmailUniqueEntity emailUnique = new EmailUniqueEntity();
        emailUnique.setPk("EMAIL#" + student.getEmail());
        emailUnique.setSk("METADATA");
        emailUnique.setStudentId(studentId);

        studentDao.deleteStudent(student, emailUnique);

        logger.info("Student deleted successfully with id: {}", studentId);
    }
}