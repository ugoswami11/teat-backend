package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.ExecuteTestCaseRequest;
import com.teat.teat_backend.dto.response.TestExecutionDTO;

import java.util.UUID;

/**
 * Service interface for TestRunTestCase (Test Execution) operations.
 */
public interface TestRunTestCaseService {

    /**
     * Execute/update a test case execution within a test run.
     */
    TestExecutionDTO executeTestCase(UUID testRunId, UUID testRunTestCaseId, ExecuteTestCaseRequest request);

    /**
     * Get test execution details.
     */
    TestExecutionDTO getTestExecutionById(UUID testRunTestCaseId);
}
