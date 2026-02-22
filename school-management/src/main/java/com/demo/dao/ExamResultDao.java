package com.demo.dao;

import com.demo.entity.ExamResultEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
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
    
    public List<ExamResultEntity> findByStudentId(String studentId) {

        List<ExamResultEntity> results = new ArrayList<>();

        QueryConditional condition =
                QueryConditional.keyEqualTo(
                        Key.builder()
                                .partitionValue("STUDENT#" + studentId)
                                .build()
                );

        PageIterable<ExamResultEntity> pages =
                resultTable.query(condition);

        pages.items().forEach(item -> {
            if (item.getSk().startsWith("RESULT#")) {
                results.add(item);
            }
        });

        return results;
    }
    
    public List<ExamResultEntity> findByExamId(String examId) {

        List<ExamResultEntity> results = new ArrayList<>();

        QueryConditional condition =
                QueryConditional.keyEqualTo(
                        Key.builder()
                                .partitionValue("EXAM#" + examId)
                                .build()
                );

        PageIterable<ExamResultEntity> pages =
                resultTable.query(condition);

        pages.items().forEach(results::add);

        return results;
    }
}