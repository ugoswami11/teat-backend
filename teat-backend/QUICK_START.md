# Quick Start Guide - TEAT Backend Phase 1

## Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 12+
- Git

## Installation & Setup

### 1. Clone/Access the Repository
```bash
cd C:\Users\ugosw\UtkarshGoswami\Programming\teat-testexecution\ tracker\repos\teat-backend\teat-backend
```

### 2. Update Database Credentials
Edit `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://YOUR_HOST:5432/YOUR_DATABASE
    username: YOUR_USERNAME
    password: YOUR_PASSWORD
```

### 3. Build the Project
```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile all Java sources
- Run unit tests (if any)
- Create target/teat-backend-0.0.1-SNAPSHOT.jar

### 4. Run Flyway Migrations
Migrations run automatically on application startup. To verify:

```bash
mvn flyway:info
```

### 5. Start the Application

#### Using Maven:
```bash
mvn spring-boot:run
```

#### Using JAR:
```bash
java -jar target/teat-backend-0.0.1-SNAPSHOT.jar
```

### 6. Verify Application is Running
Access Swagger UI: http://localhost:8080/swagger-ui.html

You should see:
- All 5 controllers listed
- All 34 endpoints documented
- Interactive API testing available

## API Testing

### Using Swagger UI (Recommended)
1. Open http://localhost:8080/swagger-ui.html
2. Click on any endpoint
3. Click "Try it out"
4. Fill in parameters
5. Click "Execute"

### Using cURL

#### Create a Test Run
```bash
curl -X POST http://localhost:8080/api/test-runs \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smoke Test Run",
    "description": "Initial smoke testing"
  }'
```

#### Get Test Runs
```bash
curl http://localhost:8080/api/test-runs?page=0&size=20
```

#### Create a Test Case
```bash
curl -X POST http://localhost:8080/api/test-cases \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Login Test",
    "objective": "Verify user login functionality",
    "expectedResult": "User successfully logged in",
    "labels": "smoke,authentication"
  }'
```

## Troubleshooting

### Port Already in Use
If port 8080 is busy, change in application.yaml:
```yaml
server:
  port: 8081
```

### Database Connection Issues
- Verify PostgreSQL is running
- Check credentials in application.yaml
- Ensure database exists
- Check connection with: `psql -U username -d database_name`

### Flyway Migration Fails
- Check database connectivity
- Verify migration file exists: `src/main/resources/db/migration/V1__Initial_Schema.sql`
- Check for syntax errors in migration file
- Clear schema if needed (be careful in production!):
  ```sql
  DROP SCHEMA public CASCADE;
  CREATE SCHEMA public;
  ```

### Compilation Errors
- Ensure Java 17 is installed: `java -version`
- Clear Maven cache: `mvn clean`
- Update Maven: `mvn --version`

## Project Structure

```
teat-backend/
├── src/
│   ├── main/
│   │   ├── java/com/teat/teat_backend/
│   │   │   ├── controller/          # REST endpoints
│   │   │   ├── service/             # Business logic
│   │   │   ├── repository/          # Data access
│   │   │   ├── entity/              # JPA entities
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── mapper/              # Entity ↔ DTO mapping
│   │   │   ├── exception/           # Custom exceptions
│   │   │   ├── util/                # Utilities
│   │   │   └── config/              # Configuration
│   │   └── resources/
│   │       ├── db/migration/        # Flyway migrations
│   │       └── application.yaml     # Configuration
│   └── test/
│       └── java/com/teat/teat_backend/
│           └── (test classes)
├── pom.xml                          # Maven configuration
├── IMPLEMENTATION_SUMMARY.md        # Implementation details
├── FILE_MANIFEST.md                 # File organization
├── VALIDATION_CHECKLIST.md          # Requirements verification
└── QUICK_START.md                   # This file
```

## Key Endpoints

### Test Runs
- `GET /api/test-runs` - List all test runs
- `POST /api/test-runs` - Create new test run
- `GET /api/test-runs/{id}` - Get test run details
- `PUT /api/test-runs/{id}` - Update test run
- `DELETE /api/test-runs/{id}` - Delete test run (soft)
- `PATCH /api/test-runs/{id}/restore` - Restore deleted run
- `POST /api/test-runs/{id}/test-cases` - Add test cases

### Test Cases
- `GET /api/test-cases` - List test cases
- `POST /api/test-cases` - Create test case
- `GET /api/test-cases/{id}` - Get test case with steps
- `PUT /api/test-cases/{id}` - Update test case
- `DELETE /api/test-cases/{id}` - Delete test case
- `GET /api/test-cases/{id}/steps` - List test steps
- `POST /api/test-cases/{id}/steps` - Create test step

### Executions
- `PUT /api/test-runs/{testRunId}/executions/{testRunTestCaseId}` - Execute test

### Action Items
- `GET /api/action-items` - List action items
- `POST /api/action-items/{id}/resolve` - Resolve action item

## Common Operations

### Create Complete Test Scenario

1. **Create Test Run**
   ```bash
   curl -X POST http://localhost:8080/api/test-runs \
     -H "Content-Type: application/json" \
     -d '{"name": "Sprint 1 Testing", "description": "E2E tests"}'
   ```
   Response: `{id: 1, ...}`

2. **Create Test Case**
   ```bash
   curl -X POST http://localhost:8080/api/test-cases \
     -H "Content-Type: application/json" \
     -d '{
       "name": "User Registration",
       "objective": "Verify user can register",
       "expectedResult": "Account created successfully",
       "labels": "authentication"
     }'
   ```
   Response: `{id: 1, ...}`

3. **Add Test Case to Run**
   ```bash
   curl -X POST http://localhost:8080/api/test-runs/1/test-cases \
     -H "Content-Type: application/json" \
     -d '{"testCaseIds": [1, 2, 3]}'
   ```
   Response: `{addedExecutions: [{testRunTestCaseId: 1, executionStatus: "NOT_EXECUTED"}, ...]}`

4. **Execute Test Case**
   ```bash
   curl -X PUT "http://localhost:8080/api/test-runs/1/executions/1" \
     -H "Content-Type: application/json" \
     -d '{
       "executionStatus": "PASS",
       "executionType": "MANUAL",
       "executedBy": "john.doe",
       "evidence": "Test passed - all assertions successful",
       "version": 0
     }'
   ```

5. **Create Action Item** (if test fails)
   ```bash
   # First, execute test with FAIL status
   curl -X PUT "http://localhost:8080/api/test-runs/1/executions/1" \
     -H "Content-Type: application/json" \
     -d '{
       "executionStatus": "FAIL",
       "evidence": "Expected 'Welcome' but got 'Error'",
       "version": 0
     }'
   
   # Then manually create action item (call service method directly or implement endpoint)
   ```

## Monitoring

### Application Logs
```bash
tail -f logs/application.log
```

### Database Queries
Enable query logging in application.yaml:
```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql: TRACE
```

### API Health Check
```bash
curl http://localhost:8080/actuator/health
```

## Performance Tips

1. **Pagination**: Always use pagination on list endpoints
   ```bash
   http://localhost:8080/api/test-runs?page=0&size=50
   ```

2. **Filtering**: Use filter parameters to reduce result sets
   ```bash
   http://localhost:8080/api/test-runs?status=COMPLETED&createdBy=abc-def
   ```

3. **Sorting**: Specify sort order
   ```bash
   http://localhost:8080/api/test-runs?sort=createdAt,desc&sort=name,asc
   ```

4. **Database Indexes**: Migrations include indexes on common query columns

## Testing

### Run Tests
```bash
mvn test
```

### Run Specific Test
```bash
mvn test -Dtest=TestRunServiceImplTest
```

### Code Coverage
```bash
mvn jacoco:report
open target/site/jacoco/index.html
```

## Useful Maven Commands

```bash
# Clean build
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Run only tests
mvn test

# Update dependencies
mvn dependency:tree

# Format code
mvn spotless:apply

# Check for security vulnerabilities
mvn dependency-check:check

# Generate project report
mvn site

# Deploy to production
mvn clean package -DskipTests
java -jar target/teat-backend-0.0.1-SNAPSHOT.jar
```

## Environment Variables

For production, set environment variables instead of modifying application.yaml:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/teat
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=prod_password
export SERVER_PORT=8080
```

## Docker Deployment (Future)

Build Docker image:
```bash
docker build -t teat-backend:1.0 .
docker run -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/teat \
           -e SPRING_DATASOURCE_USERNAME=user \
           -e SPRING_DATASOURCE_PASSWORD=password \
           -p 8080:8080 \
           teat-backend:1.0
```

## Next Steps

- Phase 2: Implement JWT Authentication
- Phase 2: Add RBAC (Role-Based Access Control)
- Phase 2: Advanced filtering with QueryDSL
- Phase 3: Performance tuning and caching
- Phase 3: Add monitoring and metrics

## Support & Documentation

- **Swagger/OpenAPI**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/v3/api-docs
- **Implementation Summary**: See IMPLEMENTATION_SUMMARY.md
- **File Manifest**: See FILE_MANIFEST.md
- **Validation Checklist**: See VALIDATION_CHECKLIST.md

---

**Version**: 1.0.0  
**Status**: Production Ready  
**Last Updated**: March 2026
