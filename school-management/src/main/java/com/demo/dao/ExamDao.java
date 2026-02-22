package com.demo.dao;

import com.demo.entity.ExamEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ExamDao {

    private static final String TABLE_NAME = "school_management";

    private final DynamoDbTable<ExamEntity> examTable;

    public ExamDao(DynamoDbEnhancedClient enhancedClient) {
        this.examTable = enhancedClient.table(
                TABLE_NAME,
                TableSchema.fromBean(ExamEntity.class)
        );
    }

    // -------------------------
    // Create Exam
    // -------------------------
    public void createExam(ExamEntity exam) {
        examTable.putItem(exam);
    }

    // -------------------------
    // Get Exam By ID
    // -------------------------
    public ExamEntity getExam(String courseId, String examId) {

        Key key = Key.builder()
                .partitionValue("COURSE#" + courseId)
                .sortValue("EXAM#" + examId)
                .build();

        return examTable.getItem(r -> r.key(key));
    }

    // -------------------------
    // List All Exams Of Course
    // -------------------------
    public List<ExamEntity> findByCourseId(String courseId) {

        List<ExamEntity> exams = new ArrayList<>();

        QueryConditional condition =
                QueryConditional.sortBeginsWith(
                        Key.builder()
                                .partitionValue("COURSE#" + courseId)
                                .sortValue("EXAM#")
                                .build()
                );

        PageIterable<ExamEntity> pages =
                examTable.query(condition);

        pages.items().forEach(exams::add);

        return exams;
    }

    // -------------------------
    // Delete Exam
    // -------------------------
    public void deleteExam(ExamEntity exam) {
        examTable.deleteItem(exam);
    }
    
    //fetch all the exams
    public List<ExamEntity> findAllExams() {

        List<ExamEntity> exams = new ArrayList<>();

        PageIterable<ExamEntity> pages =
                examTable.scan();

        pages.items().forEach(item -> {
            if (item.getSk() != null &&
                item.getSk().startsWith("EXAM#")) {
                exams.add(item);
            }
        });

        return exams;
    }
    
    
}