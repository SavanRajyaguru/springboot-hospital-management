# Hospital Management System

A robust Spring Boot application for managing hospital operations, including patient records, insurance policies, appointments, and medical staff.

## 🏥 Features

- **Patient Management**
  - Create, update, and manage patient profiles
  - Track patient medical history
  - Handle patient appointments
  - Unique validation for patient email and name

- **Insurance Management**
  - Policy creation and management
  - Automatic policy number generation
  - Integration with patient records
  - Unique validation for insurance names

- **Data Validation & Error Handling**
  - Comprehensive input validation
  - Structured error responses
  - Global exception handling
  - Duplicate resource prevention

## 🛠 Technical Stack

- **Backend Framework:** Spring Boot
- **Database:** JPA/Hibernate
- **API Documentation:** SpringDoc OpenAPI
- **Build Tool:** Maven
- **Java Version:** 17
- **Dependencies:**
  - Spring Data JPA
  - Spring Web
  - Spring Validation
  - Lombok
  - MapStruct

## 🏗 Architecture

The application follows a clean, layered architecture:

```
src/
├── main/
│   ├── java/
│   │   └── com/hostpital/hostpitalmanagement/
│   │       ├── config/         # Configuration classes
│   │       ├── controller/     # REST endpoints
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── entity/        # JPA entities
│   │       ├── exception/     # Custom exceptions & handlers
│   │       ├── mapper/        # DTO-Entity mappers
│   │       ├── repository/    # Data access layer
│   │       └── service/       # Business logic
│   └── resources/
│       └── application.properties
```

## 🚀 Key Implementation Details

### Validation System
- Service-layer validation for unique constraints
- Custom exception handling for better error messages
- Structured validation error responses

### Data Mapping
- Efficient DTO-Entity mapping using MapStruct
- Clean separation of API and domain models

### Error Handling
- Centralized exception handling
- Consistent error response format
- Detailed validation error messages

## 🔧 Setup and Installation

1. Clone the repository:
   ```bash
   git clone [repository-url]
   ```

2. Navigate to project directory:
   ```bash
   cd hospitalmanagement
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Configure the application:
   ```bash
   # Copy the template properties file
   cp src/main/resources/application.properties.template src/main/resources/application.properties
   
   # Edit application.properties with your database credentials
   # Update the following properties:
   # - spring.datasource.url
   # - spring.datasource.username
   # - spring.datasource.password
   ```

5. Run the application:
   ```bash
   java -jar target/hospitalmanagement-0.0.1-SNAPSHOT.jar
   ```

## 📝 API Documentation

Once the application is running, access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### Sample API Endpoints

#### Patient Management
- `POST /api/patients` - Create new patient
- `GET /api/patients/{id}` - Get patient by ID
- `GET /api/patients` - List all patients (with pagination)
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient

#### Insurance Management
- `POST /api/insurances` - Create new insurance
- `GET /api/insurances/{id}` - Get insurance by ID
- `GET /api/insurances` - List all insurances (with pagination)
- `PUT /api/insurances/{id}` - Update insurance
- `DELETE /api/insurances/{id}` - Delete insurance

## 💡 Best Practices Implemented

1. **Clean Code Architecture**
   - Separation of concerns
   - SOLID principles
   - DRY (Don't Repeat Yourself)

2. **Error Handling**
   - Custom exceptions
   - Structured error responses
   - Validation error handling

3. **Security**
   - Input validation
   - Data sanitization
   - Error message safety

4. **Performance**
   - Pagination for large datasets
   - Efficient database queries
   - Proper indexing

## 🎯 Future Enhancements

- Authentication and Authorization
- Role-based access control
- Appointment scheduling system
- Medical record management
- Billing and payment integration
- Reporting and analytics
- Doctor's dashboard
- Patient portal

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
