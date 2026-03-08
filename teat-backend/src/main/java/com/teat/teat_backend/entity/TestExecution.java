package com.teat.teat_backend.entity;

/**
 * DEPRECATED: This entity is kept for backward compatibility only.
 * Use {@link TestRunTestCase} instead, which is the new execution instance table
 * that properly tracks test execution within a specific test run.
 *
 * The new design separates execution instances from test definitions:
 * - TestCase: Reusable test definition (independent of test runs)
 * - TestRunTestCase: Execution instance (specific test case in a specific test run)
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public class TestExecution {
    // This class is deprecated. Use TestRunTestCase instead.
}
