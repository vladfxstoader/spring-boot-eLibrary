# 📚 eLibrary Application

eLibrary is a web-based library management system designed to facilitate the management of books, users, loans, and more. It provides a user-friendly interface for searching, borrowing, and returning books, while also offering robust administrative features.

## 🚀 Features

- **Secure Authentication**: Users can create accounts with hashed passwords. Account creation requires approval from administrators.
- **User Roles**: Administrators, librarians, and readers each have distinct roles with specific privileges.
- **User Group Management**: Administrators can create, approve, reject, and assign users to different roles.
- **Book Management**: Librarians can add, update, delete books, categories, and authors. They can also associate books with authors and categories.
- **Loan Management**: Users can search for books, request loans, view loan history, and return books. Librarians can approve or reject loan requests.
- **Data Validation and Exception Handling**: Data validation is performed on both client and server sides to prevent data loss in case of request failures. Exceptions are handled appropriately.
- **Logging**: Logs are utilized to track important operations applied to the database.
- **Pagination and Sorting**: Options for pagination and sorting of data are available.
- **Spring Security**: Authentication is implemented using Spring Security with JDBC.

## 📊 Entity Relationships

- **Author**: One-to-One relationship with AuthorDetails, Many-to-Many relationship with Book.
- **AuthorDetails**: One-to-One relationship with Author.
- **Book**: Many-to-One relationship with Publisher, Many-to-Many relationships with Author and Category, One-to-Many relationship with Loan.
- **Category**: Many-to-Many relationship with Book.
- **Loan**: Many-to-One relationships with User and Book.
- **Publisher**: One-to-Many relationship with Book.
- **Role**: Many-to-Many relationship with User.
- **User**: Many-to-Many relationships with Role and PastRole.

## 💻 Technologies Used

- **Backend**: Java, Spring Boot, Hibernate, JPA
- **Frontend**: Thymeleaf
- **Database**: MySQL Server
- **Testing**: Mockito, JUnit
- **Security**: Spring Security (JDBC authentication)
- **Logging**: SLF4J, Logback

## 🛠️ Testing

- Unit tests and integration tests are implemented using Mockito and JUnit.
- The application is tested with two profiles and databases, one being MySQL and the other H2.
