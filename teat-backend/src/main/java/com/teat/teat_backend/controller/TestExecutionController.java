package com.teat.teat_backend.controller;

import com.teat.teat_backend.dto.request.ExecuteTestCaseRequest;
import com.teat.teat_backend.dto.response.TestExecutionDTO;
import com.teat.teat_backend.service.TestRunTestCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Test Execution operations.
 */
@RestController
@RequestMapping("/api/test-runs/{testRunId}/executions")
@RequiredArgsConstructor
@Slf4j
public class TestExecutionController {

    private final TestRunTestCaseService testRunTestCaseService;

    /**
     * Execute/update a test case within a test run.
     */
    @PutMapping("/{testRunTestCaseId}")
    public ResponseEntity<TestExecutionDTO> executeTestCase(
            @PathVariable UUID testRunId,
            @PathVariable UUID testRunTestCaseId,
            @Valid @RequestBody ExecuteTestCaseRequest request) {
        log.info("PUT /test-runs/{}/executions/{} - Executing test case", testRunId, testRunTestCaseId);
        TestExecutionDTO execution = testRunTestCaseService.executeTestCase(testRunId, testRunTestCaseId, request);
        return ResponseEntity.ok(execution);
    }
}
