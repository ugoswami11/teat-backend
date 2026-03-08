package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestStepRequest;
import com.teat.teat_backend.dto.request.UpdateTestStepRequest;
import com.teat.teat_backend.dto.response.TestStepDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for TestStep operations.
 */
public interface TestStepService {

    /**
     * Create a new test step for a test case.
     */
    TestStepDTO createTestStep(UUID testCaseId, CreateTestStepRequest request);

    /**
     * Get all test steps for a test case.
     */
    List<TestStepDTO> getTestStepsByTestCaseId(UUID testCaseId);

    /**
     * Get a test step by ID.
     */
    TestStepDTO getTestStepById(UUID id);

    /**
     * Update an existing test step.
     */
    TestStepDTO updateTestStep(UUID id, UpdateTestStepRequest request);

    /**
     * Delete a test step.
     */
    void deleteTestStep(UUID id);
}
