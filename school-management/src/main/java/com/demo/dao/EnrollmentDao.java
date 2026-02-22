package com.demo.dao;

import com.demo.entity.EnrollmentEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

@Repository
public class EnrollmentDao {

    private static final String TABLE_NAME = "school_management";

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<EnrollmentEntity> enrollmentTable;

    public EnrollmentDao(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.enrollmentTable = enhancedClient.table(
                TABLE_NAME,
                TableSchema.fromBean(EnrollmentEntity.class)
        );
    }

    // -------------------------
    // Enroll Student (Transaction)
    // -------------------------
    public void enroll(EnrollmentEntity studentSide,
                       EnrollmentEntity courseSide) {

        TransactWriteItemsEnhancedRequest request =
                TransactWriteItemsEnhancedRequest.builder()
                        .addPutItem(enrollmentTable, studentSide)
                        .addPutItem(enrollmentTable, courseSide)
                        .build();

        enhancedClient.transactWriteItems(request);
    }
    
    public List<EnrollmentEntity> findByStudentId(String studentId) {

        List<EnrollmentEntity> enrollments = new ArrayList<>();

        QueryConditional queryConditional =
                QueryConditional.keyEqualTo(
                        Key.builder()
                                .partitionValue("STUDENT#" + studentId)
                                .build()
                );

        PageIterable<EnrollmentEntity> pages =
                enrollmentTable.query(queryConditional);

        pages.items().forEach(enrollments::add);

        return enrollments;
    }
    
    public List<EnrollmentEntity> findByCourseId(String courseId) {

        List<EnrollmentEntity> enrollments = new ArrayList<>();

        QueryConditional queryConditional =
                QueryConditional.keyEqualTo(
                        Key.builder()
                                .partitionValue("COURSE#" + courseId)
                                .build()
                );

        PageIterable<EnrollmentEntity> pages =
                enrollmentTable.query(queryConditional);

        pages.items().forEach(enrollments::add);

        return enrollments;
    }
}