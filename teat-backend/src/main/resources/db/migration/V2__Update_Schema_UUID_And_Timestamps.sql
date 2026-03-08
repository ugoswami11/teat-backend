-- V2__Update_Schema_UUID_And_Timestamps.sql
-- Update schema to use TIMESTAMP WITH TIME ZONE for all timestamp columns
-- This migration safely updates existing columns without destroying data
-- Created: 2026-03-08

-- ============================================================================
-- Step 1: Update action_item_resolution table timestamps
-- ============================================================================
ALTER TABLE action_item_resolution
    ALTER COLUMN created_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE action_item_resolution
    ALTER COLUMN updated_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE action_item_resolution
    ALTER COLUMN resolved_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

-- ============================================================================
-- Step 2: Update action_item table timestamps
-- ============================================================================
ALTER TABLE action_item
    ALTER COLUMN created_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE action_item
    ALTER COLUMN updated_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

-- ============================================================================
-- Step 3: Update test_run_test_case table timestamps
-- ============================================================================
ALTER TABLE test_run_test_case
    ALTER COLUMN created_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE test_run_test_case
    ALTER COLUMN updated_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE test_run_test_case
    ALTER COLUMN executed_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

-- ============================================================================
-- Step 4: Update test_step table timestamps
-- ============================================================================
ALTER TABLE test_step
    ALTER COLUMN created_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE test_step
    ALTER COLUMN updated_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

-- ============================================================================
-- Step 5: Update test_case table timestamps
-- ============================================================================
ALTER TABLE test_case
    ALTER COLUMN created_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE test_case
    ALTER COLUMN updated_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

-- ============================================================================
-- Step 6: Update test_runs table timestamps
-- ============================================================================
ALTER TABLE test_runs
    ALTER COLUMN created_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE test_runs
    ALTER COLUMN updated_at SET DATA TYPE TIMESTAMP WITH TIME ZONE;

