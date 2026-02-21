package com.demo.service;

import com.demo.dao.StudentDao;
import com.demo.dto.StudentRequest;
import com.demo.dto.StudentResponse;
import com.demo.entity.EmailUniqueEntity;
import com.demo.entity.StudentEntity;
import com.demo.exception.EmailAlreadyExistsException;
import com.demo.exception.StudentNotFoundException;

import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public StudentResponse createStudent(StudentRequest request) {

        // Generate UUID
        String studentId = UUID.randomUUID().toString();

        // Build Student Entity
        StudentEntity student = new StudentEntity();
        student.setPk("STUDENT#" + studentId);
        student.setSk("METADATA");
        student.setStudentId(studentId);
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());

        // Build Email Unique Entity
        EmailUniqueEntity emailUnique = new EmailUniqueEntity();
        emailUnique.setPk("EMAIL#" + request.getEmail());
        emailUnique.setSk("METADATA");
        emailUnique.setStudentId(studentId);

        // Save using DAO
        try {
            studentDao.createStudent(student, emailUnique);
        } catch (TransactionCanceledException e) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // Return Response
        return new StudentResponse(
                studentId,
                request.getName(),
                request.getEmail(),
                request.getAge()
        );
    }
    
    // for getting student by its id
    public StudentResponse getStudentById(String studentId) {

        StudentEntity student = studentDao.getStudent(studentId);

        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }

        return new StudentResponse(
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getAge()
        );
    }
    
    //deleting student 
    public void deleteStudent(String studentId) {

        StudentEntity student = studentDao.getStudent(studentId);

        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }

        EmailUniqueEntity emailUnique = new EmailUniqueEntity();
        emailUnique.setPk("EMAIL#" + student.getEmail());
        emailUnique.setSk("METADATA");
        emailUnique.setStudentId(studentId);

        studentDao.deleteStudent(student, emailUnique);
    }
    
    //get all the student
    public List<StudentResponse> getAllStudents() {

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

        return responses;
    }
}