# Library Management System API  

## Project Overview  
This project implements a Library Management System API using Spring Boot. The API allows librarians to manage books, patrons, and borrowing records effectively.  

---  

## Requirements  
- **Entities**:  
  - **Book**: Attributes include ID, title, author, publication year, ISBN, etc.  
  - **Patron**: Contains ID, name, contact information, etc.  
  - **Borrowing Record**: Tracks the association between books and patrons, including borrowing and return dates.  

- **API Endpoints**:  
  - **Book Management**:  
    - `GET /api/books`: Retrieve all books.  
    - `GET /api/books/{id}`: Retrieve a book by ID.  
    - `POST /api/books`: Add a new book.  
    - `PUT /api/books/{id}`: Update book information.  
    - `DELETE /api/books/{id}`: Remove a book.  
  
  - **Patron Management**:  
    - `GET /api/patrons`: Retrieve all patrons.  
    - `GET /api/patrons/{id}`: Retrieve a patron by ID.  
    - `POST /api/patrons`: Add a new patron.  
    - `PUT /api/patrons/{id}`: Update patron information.  
    - `DELETE /api/patrons/{id}`: Remove a patron.  

  - **Borrowing Operations**:  
    - `POST /api/borrow/{bookId}/patron/{patronId}`: Borrow a book.  
    - `PUT /api/return/{bookId}/patron/{patronId}`: Return a borrowed book.  

---  

## Technologies Used  
- **Spring Boot**: For building the application.  
- **Maven**: For project management and dependency management.  
- **PostgreSQL**: For data storage.  
- **JWT**: For security (basic authentication and authorization).  
- **Caffeine**: For caching.  
  
---  

## Features Implemented  
- Caching using **Caffeine** for patrons and books endpoints.  
- **JWT** for authentication, including access token, logout functionality, and authorization for book borrowing and some patron endpoints.  
- Repository design pattern for separating data access from business logic and controllers.  
- **DTOs** to specify request and response bodies.  
- **Builder Pattern** for creating and managing entities.  
- Global exception handling and input validation.  
- **Declarative transaction management** for required services.  

---  

## Testing  
> Note: Unit tests were not added as I am still learning about unit testing in Java.  

---  

## Running the Project  

### Prerequisites  
- Java 17  
- Maven  

### Installation Steps  
1. #### Clone this repository:
   git clone <repository_url>  
   cd <repository_directory>
2. #### Install dep and run project      
   mvn clean install
   mvn spring-boot:run

   
### API Documentation
  Documentation for the API endpoints can be found in the Postman collection linked below:
   **https://app.getpostman.com/join-team?invite_code=ca1562d3677c4e0c3b2aee0c6368e5a1745fcc16ab51c5c32ecfe08dc7cc0574&target_code=6eab789db4681e834e1f19c41a814d31**


### Evaluation Criteria :
1. Functionality of CRUD operations for books, patrons, and borrowing records.
2. Code quality: readability, maintainability, and adherence to best practices.
3. Error handling and validation for edge cases.
4 .Bonus features like JWT authentication, authorization, caching and transaction management.


## Additional Features (Optional - Extra Credit)  
- **Authentication & Authorization** : Implmented Auth using JWT to authenticate patrons and give them auhtority to add books or borrow books etc...  
- **Caching**: Utilized Spring's caching mechanisms and Caffeine to improve performance by caching frequently accessed data, such as book details and patron information.  

      
