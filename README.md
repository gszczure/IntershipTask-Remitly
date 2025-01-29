# SWIFT Code Management Application

---

This application is designed to manage SWIFT codes (Bank Identifier Codes, BICs) for various banks and financial institutions.

---

When the application starts, data is automatically loaded from an Excel file located in the project. The data from this file is inserted into the database during startup. The same process happens when running the integration tests — the data is loaded into the database at the beginning of the tests.

The database is configured with hibernate `ddl-auto=create`, which means that the schema is automatically created on startup, ensuring a fresh database every time the application starts.

The application was tested on two different machines before submission to ensure it works as expected. However, if upon running the command `docker-compose up`, only the database container starts, please manually start the application container by running the appropriate command inside the container.

The detailed instructions for running the application and its description are provided below.
---

---

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Docker
- Maven
- JUnit 5 for testing

---

## Features

- Add new SWIFT codes
- Retrieve SWIFT code details
- Get all SWIFT codes for a specific country
- Validate SWIFT code format

---

## API Endpoints

- `POST /v1/swift-codes/`: Add a new SWIFT code
- `GET /v1/swift-codes/{swift-code}`: Get details of a specific SWIFT code
- `GET /v1/swift-codes/country/{countryISO2code}`: Get all SWIFT codes for a specific country
- `DELETE /v1/swift-codes/{swift-code}`: Delete a SWIFT code

---

### Endpoint Details

1. **Add a new SWIFT code**
   - Method: POST
   - Path: `/v1/swift-codes/`
   - Body: JSON object containing SWIFT code details (validated)
   - Response: Success message

2. **Get details of a specific SWIFT code**
   - Method: GET
   - Path: `/v1/swift-codes/{swift-code}`
   - Response: SWIFT code details

3. **Get all SWIFT codes for a specific country**
   - Method: GET
   - Path: `/v1/swift-codes/country/{countryISO2code}`
   - Response: List of SWIFT codes for the specified country

4. **Delete a SWIFT code**
   - Method: DELETE
   - Path: `/v1/swift-codes/{swift-code}`
   - Query Parameters:
     - `bankName`: Name of the bank associated with the SWIFT code
     - `countryISO2`: ISO2 country code where the bank operates
   - Response: Success message


---

## Tests

---

### Unit Tests

The main unit test file is `SwiftCodeControllerUnitTest.java`, which contains various test cases for the controller layer.

Test categories:
- SWIFT code retrieval
- SWIFT code addition
- SWIFT code deletion
- Error handling for invalid inputs

---

### Integration Tests

The main integration test file is `SwiftCodeControllerIntegrationTest.java`, which tests the entire flow from HTTP requests to database operations.

Test categories:
- SWIFT code retrieval (including not found scenarios)
- SWIFT codes retrieval by country
- SWIFT code addition (including validation of SWIFT code format)
- SWIFT code deletion
- Error handling for various invalid inputs and edge cases

---

These tests ensure that the application behaves correctly under various conditions and maintains data integrity.

Total number of tests: 19 (including both unit and integration tests, covering positive and negative scenarios. 
There is much more that could be tested in this application. While the current tests cover the main scenarios, additional tests for edge cases, performance could further improve the test coverage.)

---

## Running the Application

To run the application using Docker, follow these steps:

1. First, you need to clone the repository:

   ```
   git clone <repository_url>
   ```
   
   After cloning the repository, follow the steps to set up and run the application.

2. Make sure you have Docker and Docker Compose installed on your system.

3. Navigate to the project root directory.

4. Run the following command to start the application and its dependencies:

   ```
   docker-compose up --build
   ```

   This command will build the Docker image for the application and start the necessary containers (application and PostgreSQL database).

5. The application should now be running and accessible at `http://localhost:8080`.
  
6. You can test the endpoints using tools such as Postman.

---

## Running Tests

To run the tests inside the Docker container, follow these steps:

1. First, make sure the application is running using (look "Running the Application" section)

2. In a new terminal window, execute the following command to enter the application container:

   ```
   docker exec -it <application_container_name> /bin/sh
   ```

   Replace `<application_container_name>` with the actual name of your application container. You can find this name by running `docker ps` and looking for the container running your Java application.

3. Once inside the container, run the tests using Maven:

   ```
   ./mvnw test
   ```

   This command will execute all the tests in the application.

4. After the tests complete, you'll see a summary of the test results in the console.
