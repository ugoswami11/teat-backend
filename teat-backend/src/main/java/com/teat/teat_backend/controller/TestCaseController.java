package com.teat.teat_backend.controller;

import com.teat.teat_backend.dto.request.CreateTestCaseRequest;
import com.teat.teat_backend.dto.request.CreateTestStepRequest;
import com.teat.teat_backend.dto.request.UpdateTestCaseRequest;
import com.teat.teat_backend.dto.response.PageResponse;
import com.teat.teat_backend.dto.response.TestCaseDTO;
import com.teat.teat_backend.dto.response.TestStepDTO;
import com.teat.teat_backend.service.TestCaseService;
import com.teat.teat_backend.service.TestStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Test Case operations.
 */
@RestController
@RequestMapping("/api/test-cases")
@RequiredArgsConstructor
@Slf4j
public class TestCaseController {

    private final TestCaseService testCaseService;
    private final TestStepService testStepService;

    /**
     * Create a new test case.
     */
    @PostMapping
    public ResponseEntity<TestCaseDTO> createTestCase(@Valid @RequestBody CreateTestCaseRequest request) {
        log.info("POST /test-cases - Creating new test case");
        TestCaseDTO testCase = testCaseService.createTestCase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(testCase);
    }

    /**
     * Get a test case by ID with its steps.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCaseDTO> getTestCaseById(@PathVariable UUID id) {
        log.info("GET /test-cases/{} - Fetching test case", id);
        TestCaseDTO testCase = testCaseService.getTestCaseById(id);
        return ResponseEntity.ok(testCase);
    }

    /**
     * Get paginated list of test cases with optional filtering.
     */
    @GetMapping
    public ResponseEntity<PageResponse<TestCaseDTO>> getAllTestCases(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String label,
            @RequestParam(required = false) String createdBy) {
        log.info("GET /test-cases - Fetching paginated test cases");
        Page<TestCaseDTO> testCases = testCaseService.getAllTestCases(pageable, name, label, createdBy);
        return ResponseEntity.ok(PageResponse.of(testCases));
    }

    /**
     * Update a test case.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCaseDTO> updateTestCase(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTestCaseRequest request) {
        log.info("PUT /test-cases/{} - Updating test case", id);
        TestCaseDTO testCase = testCaseService.updateTestCase(id, request);
        return ResponseEntity.ok(testCase);
    }

    /**
     * Delete (soft delete) a test case.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable UUID id) {
        log.info("DELETE /test-cases/{} - Soft deleting test case", id);
        testCaseService.deleteTestCase(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restore a soft-deleted test case.
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreTestCase(@PathVariable UUID id) {
        log.info("PATCH /test-cases/{}/restore - Restoring test case", id);
        testCaseService.restoreTestCase(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all test steps for a test case.
     */
    @GetMapping("/{id}/steps")
    public ResponseEntity<List<TestStepDTO>> getTestSteps(@PathVariable UUID id) {
        log.info("GET /test-cases/{}/steps - Fetching test steps", id);
        List<TestStepDTO> steps = testStepService.getTestStepsByTestCaseId(id);
        return ResponseEntity.ok(steps);
    }

    /**
     * Create a test step for a test case.
     */
    @PostMapping("/{id}/steps")
    public ResponseEntity<TestStepDTO> createTestStep(
            @PathVariable UUID id,
            @Valid @RequestBody CreateTestStepRequest request) {
        log.info("POST /test-cases/{}/steps - Creating test step", id);
        TestStepDTO step = testStepService.createTestStep(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(step);
    }
}
