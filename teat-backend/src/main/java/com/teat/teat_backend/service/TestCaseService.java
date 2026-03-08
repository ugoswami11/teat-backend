package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestCaseRequest;
import com.teat.teat_backend.dto.request.UpdateTestCaseRequest;
import com.teat.teat_backend.dto.response.TestCaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface for TestCase operations.
 */
public interface TestCaseService {

    /**
     * Create a new test case.
     */
    TestCaseDTO createTestCase(CreateTestCaseRequest request);

    /**
     * Get a test case by ID with its steps.
     */
    TestCaseDTO getTestCaseById(UUID id);

    /**
     * Get paginated list of test cases with optional filtering.
     */
    Page<TestCaseDTO> getAllTestCases(Pageable pageable, String name, String label, String createdBy);

    /**
     * Update an existing test case.
     */
    TestCaseDTO updateTestCase(UUID id, UpdateTestCaseRequest request);

    /**
     * Soft delete a test case.
     */
    void deleteTestCase(UUID id);

    /**
     * Restore a soft-deleted test case.
     */
    void restoreTestCase(UUID id);
}
