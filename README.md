
School Management System - Backend


Project Overview
-----------------------------------------------------------
This is a Backend-only School Management System built using:

- Java 17
- Spring Boot 3
- Maven
- DynamoDB (Local)
- DynamoDB Enhanced Client
- JUnit 5 & Mockito
- MockMvc for Integration Testing

The system follows a clean layered architecture and implements
industry best practices including validation, exception handling,
logging, and testing.

-----------------------------------------------------------
Architecture
-----------------------------------------------------------

The project follows a standard layered architecture:

1. Controller Layer
   - Handles HTTP requests and responses
   - Uses DTOs for request and response mapping
   - Applies validation using @Valid

2. Service Layer
   - Contains business logic
   - Performs validations related to business rules
   - Throws custom exceptions
   - Uses logging

3. DAO Layer
   - Handles DynamoDB interactions
   - Uses DynamoDB Enhanced Client
   - Manages queries, scans, and transactions

4. Entity Layer
   - Maps to DynamoDB table structure
   - Uses PK and SK for single-table design

5. Global Exception Handling
   - Implemented using @RestControllerAdvice
   - Handles validation and business exceptions
   - Returns proper HTTP status codes

-----------------------------------------------------------
Database Design (DynamoDB - Single Table Design)
-----------------------------------------------------------

Table Name:
    school_management

Primary Key Structure:
    PK (Partition Key)
    SK (Sort Key)

Design Pattern Used:
    Single Table Design

Key Patterns:

Student:
    PK = STUDENT#<studentId>
    SK = METADATA

Course:
    PK = COURSE#<courseId>
    SK = METADATA

Exam:
    PK = COURSE#<courseId>
    SK = EXAM#<examId>

Enrollment:
    PK = STUDENT#<studentId>
    SK = COURSE#<courseId>

Exam Result:
    PK = STUDENT#<studentId>
    SK = RESULT#<examId>

LSI (Local Secondary Index):
    Used for querying student results sorted by exam date.

-----------------------------------------------------------
Features Implemented
-----------------------------------------------------------

Student Module
- Create Student
- Get Student by ID
- Get All Students
- Delete Student
- Email uniqueness handling

Course Module
- Create Course
- Get Course by ID
- Get All Courses
- Delete Course

Exam Module
- Create Exam under Course
- Get Exam
- Get All Exams of Course
- Delete Exam
- Admin view: Get All Exams

Enrollment Module
- Enroll Student into Course
- List Courses of Student
- List Students of Course

Exam Result Module
- Add Exam Result
- Get Results by Student
- Get Results by Exam

-----------------------------------------------------------
Validation
-----------------------------------------------------------

Implemented using:
    - @NotBlank
    - @NotNull
    - @Email
    - @Min
    - @Valid in Controller

Validation failures are handled globally.

-----------------------------------------------------------
Exception Handling
-----------------------------------------------------------

Custom Exceptions:
    - StudentNotFoundException
    - CourseNotFoundException
    - ExamNotFoundException
    - DuplicateResultException
    - EnrollmentException

Global Exception Handler:
    - Handles validation errors (400)
    - Handles not found errors (404)
    - Handles unexpected errors (500)

-----------------------------------------------------------
Testing Strategy
-----------------------------------------------------------

1. Unit Testing
   - Tested Service layer using JUnit 5
   - DAO layer mocked using Mockito
   - Business logic tested in isolation

2. Integration Testing
   - Used @SpringBootTest
   - Used MockMvc for API testing
   - Tested Controller → Service → DAO → DynamoDB flow
   - Verified HTTP status codes and JSON responses

-----------------------------------------------------------
Logging
-----------------------------------------------------------

SLF4J Logger used in service layer.
Logs include:
    - Creation events
    - Fetch operations
    - Warnings for not found
    - Business rule violations

-----------------------------------------------------------
How to Run the Project
-----------------------------------------------------------

1. Start DynamoDB Local:

   java -Djava.library.path=./DynamoDBLocal_lib 
        -jar DynamoDBLocal.jar -sharedDb

2. Run Spring Boot application.

3. Use Postman to test APIs.

-----------------------------------------------------------
Future Improvements
-----------------------------------------------------------

- Pagination (Cursor-based using LastEvaluatedKey)
- Swagger/OpenAPI documentation
- Dockerization
- CI/CD pipeline setup
- Role-based access control
- Performance optimization

-----------------------------------------------------------
Author
-----------------------------------------------------------

Developed as a backend learning project
following clean architecture principles
and industry best practices.
