package com.demo.dao;

import com.demo.entity.EnrollmentEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
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
}