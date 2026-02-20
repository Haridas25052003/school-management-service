package com.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
public class DynamoDbConfig {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDbConfig(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    private static final String TABLE_NAME = "school_management";

    @PostConstruct
    public void createTableIfNotExists() {

        try {
            dynamoDbClient.describeTable(
                    DescribeTableRequest.builder()
                            .tableName(TABLE_NAME)
                            .build()
            );

            System.out.println("Table already exists.");

        } catch (ResourceNotFoundException e) {

            System.out.println("Creating table: " + TABLE_NAME);

            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(TABLE_NAME)
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("PK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("SK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build()
                    )
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("PK")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("SK")
                                    .keyType(KeyType.RANGE)
                                    .build()
                    )
                    .provisionedThroughput(
                            ProvisionedThroughput.builder()
                                    .readCapacityUnits(5L)
                                    .writeCapacityUnits(5L)
                                    .build()
                    )
                    .build();

            dynamoDbClient.createTable(request);

            System.out.println("Table created successfully.");
        }
    }
}