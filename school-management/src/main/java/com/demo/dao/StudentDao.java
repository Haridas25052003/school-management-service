package com.demo.dao;

import com.demo.entity.StudentEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Repository
public class StudentDao {

    private final DynamoDbTable<StudentEntity> studentTable;

    public StudentDao(DynamoDbEnhancedClient enhancedClient) {
        this.studentTable = enhancedClient.table(
                "school_management",
                TableSchema.fromBean(StudentEntity.class)
        );
    }

    public void save(StudentEntity student) {
        studentTable.putItem(student);
    }

    public StudentEntity getById(String studentId) {

        Key key = Key.builder()
                .partitionValue("STUDENT#" + studentId)
                .sortValue("METADATA")
                .build();

        return studentTable.getItem(r -> r.key(key));
    }

    public void delete(String studentId) {

        Key key = Key.builder()
                .partitionValue("STUDENT#" + studentId)
                .sortValue("METADATA")
                .build();

        studentTable.deleteItem(key);
    }
}