//package com.demo.test;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//
//@Component
//public class DynamoDBConnection {
//
//    private final DynamoDbClient dynamoDbClient;
//
//    public DynamoDBConnection(DynamoDbClient dynamoDbClient) {
//        this.dynamoDbClient = dynamoDbClient;
//    }
//
//    @PostConstruct
//    public void testConnection() {
//        System.out.println("Listing tables from DynamoDB...");
//        System.out.println(dynamoDbClient.listTables().tableNames());
//    }
//}