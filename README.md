# Test Evidence and Action Tracker
*A lightweight system to track test executions, evidence, and follow-up actions.*

### Problem Statement
Manual testers and SDETs often struggle to track test executions, attach evidence, and manage follow-up actions in a lightweight and structured way. Existing tools are either too heavy or not automation-friendly.

TEAT solves this by providing a simple, test-centric platform to manage test runs, execution results, evidence, and action items. The system is designed to be fully testable via both UI and REST APIs.

### Repository Scope
This repository contains the backend services for TEAT, including:
- REST APIs for test runs, executions, and action items
- Database integration using Supabase (PostgreSQL)
- API documentation using Swagger (OpenAPI)

### Tech Stack
- Java
- Spring Boot
- Supabase (PostgreSQL)
- Swagger (OpenAPI)

### Architecture
React UI → Spring Boot API → Supabase (PostgreSQL)

### High-Level Workflow
1. Create a Test Run  
2. Add Test Cases  
3. Execute Test Cases  
4. Attach Evidence  
5. Create Action Items for failures
