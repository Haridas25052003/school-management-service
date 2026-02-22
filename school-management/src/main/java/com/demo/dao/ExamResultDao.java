package com.demo.dao;

import com.demo.entity.ExamResultEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

@Repository
public class ExamResultDao {

    private static final String TABLE_NAME = "school_management";

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<ExamResultEntity> resultTable;

    public ExamResultDao(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.resultTable = enhancedClient.table(
                TABLE_NAME,
                TableSchema.fromBean(ExamResultEntity.class)
        );
    }

    // -------------------------
    // Create Exam Result (Transaction)
    // -------------------------
    public void createResult(ExamResultEntity studentSide,
                             ExamResultEntity examSide) {

        TransactWriteItemsEnhancedRequest request =
                TransactWriteItemsEnhancedRequest.builder()
                        .addPutItem(resultTable, studentSide)
                        .addPutItem(resultTable, examSide)
                        .build();

        enhancedClient.transactWriteItems(request);
    }
}