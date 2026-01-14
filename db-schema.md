## Core Tables
We will create 4 tables:
1. test_runs
2. test_cases
3. test_executions
4. action_items

## Table schemas
1. **test_runs table**

    #### Purpose
    Represents a single testing cycle (e.g. Regression Run, Release Smoke).

    Columns
    
    |Column	|Type	|Notes|
    |-------|-------|-----|
    |id	|UUID	|Primary Key|
    |name	|VARCHAR	|e.g. “Release 1.2 Regression”|
    |description	|TEXT	|Optional|
    |created_at	|TIMESTAMP	|Auto|
    |created_by	|VARCHAR	|Optional (no auth yet)|
   
    Why this matters
    - Acts as the top-level container
    - “Test runs allow grouping executions by release or cycle.”

2. **test_cases table**
    
    #### Purpose
    Stores logical test cases, reusable across runs.
    
    Columns
    
    |Column	|Type	|Notes|
    |-------|-------|-----|
    |id	|UUID	|Primary Key|
    |test_run_id	|UUID	|FK → test_runs.id|
    |title	|VARCHAR	|Short description|
    |description	|TEXT	|Detailed steps|
    |created_at	|TIMESTAMP	|Auto|
    
    Relationship
    - One TestRun → Many TestCases

    Design decision
    - We’re scoping test cases inside test runs for simplicity.
    - “For MVP, test cases are run-scoped; they can be globalized later.”

3. test_executions table (🔥 MOST IMPORTANT)
    #### Purpose
    Represents actual execution result of a test case.
    
    Columns
    
    |Column	|Type	|Notes|
    |-------|-------|-----|
    |id	|UUID	|Primary Key|
    |test_case_id	|UUID	|FK → test_cases.id|
    |status	|VARCHAR	|PASS / FAIL / BLOCKED|
    |evidence	|TEXT	|Link or notes|
    |executed_at	|TIMESTAMP	|Auto|
    |executed_by	|VARCHAR	|Optional|
    
    Constraints
    - status should be ENUM-like
    - One execution per test case (for MVP)
    - “Executions are immutable records representing test outcomes.”

4. action_items table

    #### Purpose
    Tracks follow-up actions for failed tests.
    
    Columns
    
    |Column	|Type	|Notes|
    |-------|-------|-----|
    |id	|UUID	|Primary Key|
    |test_execution_id	|UUID	|FK → test_executions.id|
    |description	|TEXT	|Action needed|
    |status	|VARCHAR	|OPEN / IN_PROGRESS / CLOSED|
    |created_at	|TIMESTAMP	|Auto|
    
    Relationship
    - One TestExecution → Zero or One ActionItem

    Business rule
    - Action item only exists if execution = FAIL
    - This will later become a validation rule in API + automation 👌

### ENTITY RELATIONSHIP DIAGRAM
```
test_runs
  └── test_cases
        └── test_executions
              └── action_items
```


🛠️ Supabase SQL (Queries)

```
create table test_runs (
  id uuid primary key default gen_random_uuid(),
  name varchar(255) not null,
  description text,
  created_at timestamp default now(),
  created_by varchar(100)
);

create table test_cases (
  id uuid primary key default gen_random_uuid(),
  test_run_id uuid references test_runs(id) on delete cascade,
  title varchar(255) not null,
  description text,
  created_at timestamp default now()
);

create table test_executions (
  id uuid primary key default gen_random_uuid(),
  test_case_id uuid references test_cases(id) on delete cascade,
  status varchar(20) not null,
  evidence text,
  executed_at timestamp default now(),
  executed_by varchar(100)
);

create table action_items (
  id uuid primary key default gen_random_uuid(),
  test_execution_id uuid references test_executions(id) on delete cascade,
  description text not null,
  status varchar(20) default 'OPEN',
  created_at timestamp default now()
);
```