#### BASE URL

```/api/v1```

#### API Endpoints

- Test Run APIs
    
    - Create Test Run

        **POST** ```/api/v1/test-runs```
        
        Request
        ```
        {
        "name": "Release 1.0 Regression",
        "description": "Full regression testing for release 1.0",
        "createdBy": "utkarsh"
        }
        ```
        Response – 201 CREATED
        ```
        {
        "id": "uuid",
        "name": "Release 1.0 Regression",
        "description": "Full regression testing for release 1.0",
        "createdAt": "2026-01-13T10:15:30"
        }
        ```
        *Validation*
        
        ```name → required, min 3 chars```

    - Get All Test Runs

        **GET** ```/api/v1/test-runs```
        
        Response – 200 OK
        ```
        [
            {
                "id": "uuid",
                "name": "Release 1.0 Regression",
                "createdAt": "2026-01-13T10:15:30"
            }
        ]
        ```

- Test Case APIs

    - Create Test Case

        **POST** ```/api/v1/test-runs/{testRunId}/test-cases```
        
        Request
        ```
        {
        "title": "Verify login with valid credentials",
        "description": "User logs in with valid username and password"
        }
        ```
        Response – 201 CREATED
        ```
        {
        "id": "uuid",
        "testRunId": "uuid",
        "title": "Verify login with valid credentials",
        "createdAt": "2026-01-13T10:20:00"
        }
        ```
        *Validation*
        - testRunId must exist
        - title required

    - Get Test Cases by Test Run

        **GET** ```/api/v1/test-runs/{testRunId}/test-cases```

- Test Execution APIs (Core of the system)
    - Execute Test Case

        **POST** ```/api/v1/test-cases/{testCaseId}/executions```
        Request
        ```
        {
        "status": "FAIL",
        "evidence": "Login button not clickable",
        "executedBy": "utkarsh"
        }
        ```
        Response – 201 CREATED
        ```
        {
        "id": "uuid",
        "testCaseId": "uuid",
        "status": "FAIL",
        "executedAt": "2026-01-13T10:30:00"
        }
        ```
        Validation (IMPORTANT)
        - status ∈ PASS | FAIL | BLOCKED
        - Only one execution per test case (MVP)

    - Get Executions by Test Case

        **GET** ```/api/v1/test-cases/{testCaseId}/executions```

- Action Item APIs
    - Create Action Item (Only if FAIL)

        **POST** ```/api/v1/test-executions/{executionId}/action-items```
        Request
        ```
        {
        "description": "Investigate login button issue"
        }
        ```
        Response – 201 CREATED
        ```
        {
        "id": "uuid",
        "executionId": "uuid",
        "status": "OPEN",
        "createdAt": "2026-01-13T10:35:00"
        }
        ```
        *Business Rule*
        - Allowed only if execution status = FAIL
        *You’ll enforce this in:*
        - Service layer
        - API tests

    - Get Action Items

        **GET** ```/api/v1/action-items```

        *Optional filters later:*
        - /action-items?status=OPEN

- Error Handling (DO NOT SKIP)

    - Standard error response (use everywhere):
    ```
        {
        "timestamp": "2026-01-13T10:40:00",
        "status": 400,
        "error": "Bad Request",
        "message": "Invalid status value",
        "path": "/api/v1/test-executions"
        }
    ```

