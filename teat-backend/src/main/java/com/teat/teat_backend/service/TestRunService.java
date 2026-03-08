package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestRunRequest;
import com.teat.teat_backend.dto.request.UpdateTestRunRequest;
import com.teat.teat_backend.dto.response.AddTestCasesToRunResponseDTO;
import com.teat.teat_backend.dto.response.TestRunDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for TestRun operations.
 */
public interface TestRunService {

    /**
     * Create a new test run.
     */
    TestRunDTO createTestRun(CreateTestRunRequest request);

    /**
     * Get a test run by ID.
     */
    TestRunDTO getTestRunById(UUID id);

    /**
     * Get paginated list of test runs with optional filtering.
     */
    Page<TestRunDTO> getAllTestRuns(Pageable pageable, String name, String status, String createdBy);

    /**
     * Update an existing test run.
     */
    TestRunDTO updateTestRun(UUID id, UpdateTestRunRequest request);

    /**
     * Soft delete a test run.
     */
    void deleteTestRun(UUID id);

    /**
     * Restore a soft-deleted test run.
     */
    void restoreTestRun(UUID id);

    /**
     * Add test cases to a test run.
     */
    AddTestCasesToRunResponseDTO addTestCasesToRun(UUID testRunId, List<UUID> testCaseIds);
}
