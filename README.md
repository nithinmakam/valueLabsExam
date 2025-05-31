# Tracking Number Generator API

A Spring Boot API for generating unique tracking numbers for parcels based on query parameters. The API uses an in-memory cache to ensure uniqueness, validates inputs, and provides Swagger UI for documentation and testing. Deployed locally and on Azure Web App.

## Features

- Generates unique tracking numbers (10-16 alphanumeric characters).
- Validates query parameters (e.g., ISO 3166-1 alpha-2 country codes, UUID, weight).
- Sets `createdAt` to the request receipt time (server time, RFC 3339 format, no milliseconds).
- Uses Spring Cache (`ConcurrentMapCache`) for tracking number uniqueness.
- Provides Swagger UI for interactive API documentation.
- Handles validation and missing parameter errors with consistent JSON responses.
- Deployed on Azure Web App for production access.

## Prerequisites

- **Java 17** or higher
- **Maven** 3.6.0 or higher
- **Git** (for cloning the repository)
- An IDE (e.g., IntelliJ IDEA, Eclipse, VS Code) with the **Lombok plugin** installed (required for Lombok annotations)
- Internet connection for Maven dependency downloads and GitHub/Azure access
- **Azure CLI** (optional, for deploying to Azure)

## Getting Started

### Clone the Repository

1. Clone the repository from GitHub:

   ```bash
   https://github.com/nithinmakam/valueLabsExam.git
   ```

2. Navigate to the project directory:

   ```bash
   cd valueLabsExam
   ```

## Project Structure

```
valueLabsExam/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/valuelabs/exam/
│       │       ├── ExamApplication.java
│       │       ├── dto/
│       │       │   ├── TrackingNumberRequest.java
│       │       │   └── TrackingNumberResponse.java
│       │       ├── service/
│       │       │   └── TrackingNumberService.java
│       │       ├── controller/
│       │       │   └── TrackingNumberController.java
│       │       ├── config/
│       │       │   └── SwaggerConfig.java
│       │       └── exception/
│       │           └── GlobalExceptionHandler.java
│       └── resources/
│           └── application.properties
├── .gitignore
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Install Dependencies**

   - In the project root (where `pom.xml` is located), run:

     ```bash
     mvn clean install
     ```
   - This downloads all dependencies (Spring Boot 3.5.0, Springdoc 2.8.6, Lombok, etc.) and builds the project.

2. **Configure IDE**

   - Import the project as a Maven project in your IDE.
   - Enable **annotation processing** for Lombok:
     - IntelliJ IDEA: `Settings > Build, Execution, Deployment > Compiler > Annotation Processors` &gt; Enable.
     - Eclipse: Install the Lombok plugin and enable annotation processing.
   - Refresh the Maven project to resolve dependencies (`mvn idea:idea` or `mvn eclipse:eclipse` if needed).

3. **Verify Configuration**

   - Check `src/main/resources/application.properties`:

     ```properties
      spring.application.name=valueLabsExam
      spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
      server.servlet.context-path=/trackingNumberApp
     ```
   - Ensure Java 17 is set in your IDE and `pom.xml` (`java.version=17`).

## Running the Application

1. **Run Locally with Maven**

   - Execute:

     ```bash
     mvn spring-boot:run
     ```
   - The application starts on `http://localhost:8080/trackingNumberApp`.

2. **Run with IDE**

   - Open `ExamApplication.java` in your IDE.
   - Right-click and select `Run ExamApplication.main()`.
   - The application starts on `http://localhost:8080/trackingNumberApp`.

3. **Verify Startup**

   - Look for logs:

     ```
     Started ExamApplication in X.XXX seconds
     ```
   - Access Swagger UI locally at `http://localhost:8080/trackingNumberApp/swagger-ui/index.html` to confirm the API is up.

4. **Access on Azure**

   - The application is deployed on Azure Web App at `https://valuelabsexam.azurewebsites.net/trackingNumberApp`.
   - Access Swagger UI on Azure at `https://valuelabsexam.azurewebsites.net/trackingNumberApp/swagger-ui/index.html`.

## Testing the Application

The API exposes a single endpoint: `GET /next-tracking-number`. Test it using Swagger UI or curl, both locally and on Azure.

### Using Swagger UI

1. **Local**:

   - Open `http://localhost:8080/trackingNumberApp/swagger-ui/index.html`.
   - Expand the `GET /next-tracking-number` endpoint under `Tracking Number API`.
   - Click `Try it out` and enter:
     - `originCountryId`: `MY` (2-letter ISO country code, uppercase)
     - `destinationCountryId`: `ID` (2-letter ISO country code, uppercase)
     - `weight`: `1.234` (positive number, up to 3 decimal places)
     - `customerId`: `de619854-b59b-425e-9db4-943979e1bd49` (valid UUID)
     - `customerName`: `RedBox Logistics` (non-empty string)
     - `customerSlug`: `redbox-logistics` (kebab-case string)
   - Click `Execute` and verify the response:

     ```json
     {
         "trackingNumber": "XYZ123456789",
         "originCountryId": "MY",
         "destinationCountryId": "ID",
         "weight": 1.234,
         "customerId": "de619854-b59b-425e-9db4-943979e1bd49",
         "customerName": "RedBox Logistics",
         "customerSlug": "redbox-logistics",
         "createdAt": "2025-05-31T22:04:00+05:30",
         "generatedAt": "2025-05-31T22:04:00+05:30"
     }
     ```

2. **Azure**:

   - Open `https://valuelabsexam.azurewebsites.net/trackingNumberApp/swagger-ui/index.html`.
   - Follow the same steps as above to test the endpoint.

### Using curl

1. **Local Valid Request**:

   ```bash
   curl "http://localhost:8080/trackingNumberApp/next-tracking-number?originCountryId=MY&destinationCountryId=ID&weight=1.234&customerId=de619854-b59b-425e-9db4-943979e1bd49&customerName=RedBox%20Logistics&customerSlug=redbox-logistics"
   ```

   - Expected response:

     ```json
     {
         "trackingNumber": "XYZ123456789",
         "originCountryId": "MY",
         "destinationCountryId": "ID",
         "weight": 1.234,
         "customerId": "de619854-b59b-425e-9db4-943979e1bd49",
         "customerName": "RedBox Logistics",
         "customerSlug": "redbox-logistics",
         "createdAt": "2025-05-31T22:04:00+05:30",
         "generatedAt": "2025-05-31T22:04:00+05:30"
     }
     ```

2. **Azure Valid Request**:

   ```bash
   curl "https://valuelabsexam.azurewebsites.net/trackingNumberApp/next-tracking-number?originCountryId=MY&destinationCountryId=ID&weight=1.234&customerId=de619854-b59b-425e-9db4-943979e1bd49&customerName=RedBox%20Logistics&customerSlug=redbox-logistics"
   ```

   - Expected response: Same as above.

3. **Invalid Parameter** (e.g., `originCountryId=MY1`):

   - Local:

     ```bash
     curl "http://localhost:8080/trackingNumberApp/next-tracking-number?originCountryId=MY1&destinationCountryId=ID&weight=1.234&customerId=de619854-b59b-425e-9db4-943979e1bd49&customerName=RedBox%20Logistics&customerSlug=redbox-logistics"
     ```
   - Azure:

     ```bash
     curl "https://valuelabsexam.azurewebsites.net/trackingNumberApp/next-tracking-number?originCountryId=MY1&destinationCountryId=ID&weight=1.234&customerId=de619854-b59b-425e-9db4-943979e1bd49&customerName=RedBox%20Logistics&customerSlug=redbox-logistics"
     ```
   - Expected response (HTTP 400):

     ```json
     {
         "trackingNumber": "Error: Invalid origin country code",
         "originCountryId": null,
         "destinationCountryId": null,
         "weight": null,
         "customerId": null,
         "customerName": null,
         "customerSlug": null,
         "createdAt": null,
         "generatedAt": null
     }
     ```

4. **Missing Parameter** (e.g., omit `originCountryId`):

   - Local:

     ```bash
     curl "http://localhost:8080/trackingNumberApp/next-tracking-number?destinationCountryId=ID&weight=1.234&customerId=de619854-b59b-425e-9db4-943979e1bd49&customerName=RedBox%20Logistics&customerSlug=redbox-logistics"
     ```
   - Azure:

     ```bash
     curl "https://valuelabsexam.azurewebsites.net/trackingNumberApp/next-tracking-number?destinationCountryId=ID&weight=1.234&customerId=de619854-b59b-425e-9db4-943979e1bd49&customerName=RedBox%20Logistics&customerSlug=redbox-logistics"
     ```
   - Expected response (HTTP 400):

     ```json
     {
         "trackingNumber": "Error: Missing required parameter: originCountryId",
         "originCountryId": null,
         "destinationCountryId": null,
         "weight": null,
         "customerId": null,
         "customerName": null,
         "customerSlug": null,
         "createdAt": null,
         "generatedAt": null
     }
     ```

## Troubleshooting

1. **Clone Failures**

   - Ensure Git is installed (`git --version`).
   - Verify the repository URL and access permissions (public or private with authentication).

2. **Build Failures**

   - Confirm Java 17 and Maven are installed (`java -version`, `mvn -version`).
   - Run `mvn clean install` to resolve dependencies.
   - Check `pom.xml` for errors and refresh the Maven project in your IDE.

3. **Lombok Issues**

   - If getters/setters are missing, ensure the Lombok plugin is installed and annotation processing is enabled.
   - Verify `lombok` dependency in `pom.xml` (`1.18.34`).

4. **Swagger UI Not Loading**

   - Locally: Access `http://localhost:8080/trackingNumberApp/swagger-ui/index.html`.
   - Azure: Access `https://valuelabsexam.azurewebsites.net/trackingNumberApp/swagger-ui/index.html`.
   - Clear browser cache or check `application.properties` for correct Springdoc paths.

## Notes

- **Scalability**: The in-memory cache (`ConcurrentMapCache`) ensures uniqueness within a single instance. For distributed systems, consider a database or distributed cache.
- **API Documentation**: Full OpenAPI spec available at:
  - Local: `http://localhost:8080/trackingNumberApp/v3/api-docs`
  - Azure: `https://valuelabsexam.azurewebsites.net/trackingNumberApp/v3/api-docs`
