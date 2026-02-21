package com.demo.dao;

import com.demo.entity.EmailUniqueEntity;
import com.demo.entity.StudentEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Repository
public class StudentDao {

    private static final String TABLE_NAME = "school_management";

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<StudentEntity> studentTable;
    private final DynamoDbTable<EmailUniqueEntity> emailTable;

    public StudentDao(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;

        this.studentTable = enhancedClient.table(
                TABLE_NAME,
                TableSchema.fromBean(StudentEntity.class)
        );

        this.emailTable = enhancedClient.table(
                TABLE_NAME,
                TableSchema.fromBean(EmailUniqueEntity.class)
        );
    }

    // -------------------------
    // Create Student (Transaction)
    // -------------------------
    public void createStudent(StudentEntity student,
                              EmailUniqueEntity emailUnique) {

        TransactWriteItemsEnhancedRequest request =
                TransactWriteItemsEnhancedRequest.builder()
                        .addPutItem(studentTable, student)
                        .addPutItem(emailTable, emailUnique)
                        .build();

        enhancedClient.transactWriteItems(request);
    }

    // -------------------------
    // Get Student
    // -------------------------
    public StudentEntity getStudent(String studentId) {

        Key key = Key.builder()
                .partitionValue("STUDENT#" + studentId)
                .sortValue("METADATA")
                .build();

        return studentTable.getItem(r -> r.key(key));
    }

    // -------------------------
    // Delete Student (Transaction)
    // -------------------------
    public void deleteStudent(StudentEntity student,
                              EmailUniqueEntity emailUnique) {

        TransactWriteItemsEnhancedRequest request =
                TransactWriteItemsEnhancedRequest.builder()
                        .addDeleteItem(studentTable, student)
                        .addDeleteItem(emailTable, emailUnique)
                        .build();

        enhancedClient.transactWriteItems(request);
    }
    
    //get all the student
    public List<StudentEntity> getAllStudents() {

        List<StudentEntity> students = new ArrayList<>();

        PageIterable<StudentEntity> pages =
                studentTable.scan();

        pages.items().forEach(item -> {
            if (item.getPk().startsWith("STUDENT#")
                    && "METADATA".equals(item.getSk())) {
                students.add(item);
            }
        });

        return students;
    }
}