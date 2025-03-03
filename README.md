# Contact Service Backend

This is a backend service for managing contacts using **Spring Boot**, **Java 17**, **Maven**, and **Spring Data JPA**. The service provides **CRUD (Create, Read, Update, Delete) operations** for contacts and includes an API for uploading contact photos.

## Features
- **CRUD operations** for managing contacts
- **REST API** using Spring Boot
- **Spring Data JPA** for database interactions
- **Photo upload controller** for adding contact images
- **Maven-based project**
- Runs on **Java 17**

## Tech Stack
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Maven**
- **REST API**

## Getting Started

### Prerequisites
Ensure you have the following installed:
- Java 17
- Maven
- Any database (H2, MySQL, PostgreSQL, etc.)

### Clone the Repository
```sh
git clone https://github.com/your-username/contact-service.git
cd contact-service
```

### Build and Run Locally

1. **Build the project** using Maven:
   ```sh
   mvn clean install
   ```
2. **Run the application**:
   ```sh
   mvn spring-boot:run
   ```

### API Endpoints

| HTTP Method | Endpoint            | Description                     |
|------------|--------------------|---------------------------------|
| GET        | `/contacts`        | Get all contacts               |
| GET        | `/contacts/{id}`   | Get a contact by ID            |
| POST       | `/contacts`        | Create a new contact           |
| PUT        | `/contacts/{id}`   | Update an existing contact     |
| DELETE     | `/contacts/{id}`   | Delete a contact by ID         |
| POST       | `/contacts/upload` | Upload a photo for a contact   |

### Upload Contact Photo
To upload a contact photo, send a **POST** request to `/contacts/upload` with a multipart file.

## Database Configuration
You can configure the database in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/contactdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```
Replace `yourpassword` with your actual database password.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.