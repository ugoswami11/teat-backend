package com.teat.teat_backend.controller;

import com.teat.teat_backend.dto.request.AddTestCasesToRunRequest;
import com.teat.teat_backend.dto.request.CreateTestRunRequest;
import com.teat.teat_backend.dto.request.UpdateTestRunRequest;
import com.teat.teat_backend.dto.response.AddTestCasesToRunResponseDTO;
import com.teat.teat_backend.dto.response.PageResponse;
import com.teat.teat_backend.dto.response.TestRunDTO;
import com.teat.teat_backend.service.TestRunService;
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

import java.util.UUID;

/**
 * REST Controller for Test Run operations.
 */
@RestController
@RequestMapping("/api/test-runs")
@RequiredArgsConstructor
@Slf4j
public class TestRunController {

    private final TestRunService testRunService;

    /**
     * Create a new test run.
     */
    @PostMapping
    public ResponseEntity<TestRunDTO> createTestRun(@Valid @RequestBody CreateTestRunRequest request) {
        log.info("POST /test-runs - Creating new test run");
        TestRunDTO testRun = testRunService.createTestRun(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(testRun);
    }

    /**
     * Get a test run by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestRunDTO> getTestRunById(@PathVariable UUID id) {
        log.info("GET /test-runs/{} - Fetching test run", id);
        TestRunDTO testRun = testRunService.getTestRunById(id);
        return ResponseEntity.ok(testRun);
    }

    /**
     * Get paginated list of test runs with optional filtering.
     */
    @GetMapping
    public ResponseEntity<PageResponse<TestRunDTO>> getAllTestRuns(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String createdBy) {
        log.info("GET /test-runs - Fetching paginated test runs");
        Page<TestRunDTO> testRuns = testRunService.getAllTestRuns(pageable, name, status, createdBy);
        return ResponseEntity.ok(PageResponse.of(testRuns));
    }

    /**
     * Update a test run.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestRunDTO> updateTestRun(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTestRunRequest request) {
        log.info("PUT /test-runs/{} - Updating test run", id);
        TestRunDTO testRun = testRunService.updateTestRun(id, request);
        return ResponseEntity.ok(testRun);
    }

    /**
     * Delete (soft delete) a test run.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestRun(@PathVariable UUID id) {
        log.info("DELETE /test-runs/{} - Soft deleting test run", id);
        testRunService.deleteTestRun(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restore a soft-deleted test run.
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreTestRun(@PathVariable UUID id) {
        log.info("PATCH /test-runs/{}/restore - Restoring test run", id);
        testRunService.restoreTestRun(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add test cases to a test run.
     */
    @PostMapping("/{id}/test-cases")
    public ResponseEntity<AddTestCasesToRunResponseDTO> addTestCasesToRun(
            @PathVariable UUID id,
            @Valid @RequestBody AddTestCasesToRunRequest request) {
        log.info("POST /test-runs/{}/test-cases - Adding test cases to run", id);
        AddTestCasesToRunResponseDTO response = testRunService.addTestCasesToRun(id, request.getTestCaseIds());
        return ResponseEntity.ok(response);
    }
}
