-- V1__Initial_Schema.sql
-- TEAT Database Schema - Phase 1
-- Created: 2026-03-07

-- Create Enum Types
CREATE TYPE execution_status_enum AS ENUM ('NOT_EXECUTED', 'PASS', 'FAIL', 'BLOCKED');
CREATE TYPE execution_type_enum AS ENUM ('MANUAL', 'AUTOMATION');
CREATE TYPE test_run_status_enum AS ENUM ('CREATED', 'IN_PROGRESS', 'COMPLETED');
CREATE TYPE action_item_status_enum AS ENUM ('OPEN', 'IN_PROGRESS', 'CLOSED');

-- ============================================================================
-- Table: test_runs
-- Purpose: Represents a test execution cycle, defines scope and context
-- ============================================================================
CREATE TABLE test_runs (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'CREATED',
    created_by UUID NOT NULL,
    updated_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_test_runs_created_by ON test_runs(created_by);
CREATE INDEX idx_test_runs_status ON test_runs(status);
CREATE INDEX idx_test_runs_is_deleted ON test_runs(is_deleted);

-- ============================================================================
-- Table: test_case
-- Purpose: Stores reusable test definitions
-- ============================================================================
CREATE TABLE test_case (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    objective TEXT,
    expected_result TEXT,
    labels VARCHAR(255),
    created_by UUID NOT NULL,
    updated_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_test_case_created_by ON test_case(created_by);
CREATE INDEX idx_test_case_labels ON test_case(labels);
CREATE INDEX idx_test_case_is_deleted ON test_case(is_deleted);

-- ============================================================================
-- Table: test_step
-- Purpose: Stores structured steps for each test case
-- ============================================================================
CREATE TABLE test_step (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    test_case_id BIGINT NOT NULL,
    step_order INT NOT NULL,
    step_description TEXT NOT NULL,
    expected_result TEXT,
    created_by UUID NOT NULL,
    updated_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (test_case_id) REFERENCES test_case(id) ON DELETE CASCADE
);

CREATE INDEX idx_test_step_test_case_id ON test_step(test_case_id);
CREATE INDEX idx_test_step_step_order ON test_step(step_order);
CREATE INDEX idx_test_step_is_deleted ON test_step(is_deleted);

-- ============================================================================
-- Table: test_run_test_case
-- Purpose: Represents execution of a specific test case within a specific test run
--          This is the execution instance table
-- ============================================================================
CREATE TABLE test_run_test_case (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    test_run_id BIGINT NOT NULL,
    test_case_id BIGINT NOT NULL,
    execution_status VARCHAR(50) NOT NULL DEFAULT 'NOT_EXECUTED',
    execution_type VARCHAR(50),
    executed_by VARCHAR(100),
    executed_at TIMESTAMP WITH TIME ZONE,
    evidence TEXT NOT NULL,
    created_by UUID NOT NULL,
    updated_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (test_run_id) REFERENCES test_runs(id) ON DELETE CASCADE,
    FOREIGN KEY (test_case_id) REFERENCES test_case(id) ON DELETE CASCADE,
    UNIQUE (test_run_id, test_case_id)
);

CREATE INDEX idx_test_run_test_case_test_run_id ON test_run_test_case(test_run_id);
CREATE INDEX idx_test_run_test_case_test_case_id ON test_run_test_case(test_case_id);
CREATE INDEX idx_test_run_test_case_execution_status ON test_run_test_case(execution_status);
CREATE INDEX idx_test_run_test_case_is_deleted ON test_run_test_case(is_deleted);

-- ============================================================================
-- Table: action_item
-- Purpose: Automatically created when a test execution fails
--          Tracks issues and their resolution
-- ============================================================================
CREATE TABLE action_item (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    test_run_test_case_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    assigned_to VARCHAR(100),
    created_by UUID NOT NULL,
    updated_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (test_run_test_case_id) REFERENCES test_run_test_case(id) ON DELETE CASCADE
);

CREATE INDEX idx_action_item_test_run_test_case_id ON action_item(test_run_test_case_id);
CREATE INDEX idx_action_item_status ON action_item(status);
CREATE INDEX idx_action_item_is_deleted ON action_item(is_deleted);

-- ============================================================================
-- Table: action_item_resolution
-- Purpose: Stores resolution proof for closed action items
--          Provides audit trail for fixes
-- ============================================================================
CREATE TABLE action_item_resolution (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    action_item_id BIGINT NOT NULL,
    resolution_details TEXT NOT NULL,
    evidence_link VARCHAR(500),
    resolved_by VARCHAR(100) NOT NULL,
    created_by UUID NOT NULL,
    updated_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    resolved_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (action_item_id) REFERENCES action_item(id) ON DELETE CASCADE
);

CREATE INDEX idx_action_item_resolution_action_item_id ON action_item_resolution(action_item_id);
CREATE INDEX idx_action_item_resolution_is_deleted ON action_item_resolution(is_deleted);

-- ============================================================================
-- Add Triggers for updated_at timestamp
-- ============================================================================
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER test_runs_update_timestamp
    BEFORE UPDATE ON test_runs
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER test_case_update_timestamp
    BEFORE UPDATE ON test_case
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER test_step_update_timestamp
    BEFORE UPDATE ON test_step
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER test_run_test_case_update_timestamp
    BEFORE UPDATE ON test_run_test_case
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER action_item_update_timestamp
    BEFORE UPDATE ON action_item
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER action_item_resolution_update_timestamp
    BEFORE UPDATE ON action_item_resolution
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
