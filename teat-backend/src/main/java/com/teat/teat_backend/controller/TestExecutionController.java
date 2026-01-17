package com.teat.teat_backend.controller;

import com.teat.teat_backend.dto.request.CreateTestExecutionRequest;
import com.teat.teat_backend.dto.response.TestExecutionResponse;
import com.teat.teat_backend.service.TestExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test-cases/{testCaseId}/executions")
@RequiredArgsConstructor
public class TestExecutionController {

    private final TestExecutionService testExecutionService;

    @PostMapping
    public ResponseEntity<TestExecutionResponse> executeTestCase(@PathVariable UUID testCaseId,
                                                                 @Valid @RequestBody CreateTestExecutionRequest request) {
        // Implementation goes here
        return ResponseEntity.status(HttpStatus.CREATED).body(testExecutionService.executeTestCase(testCaseId, request));
    }

    @GetMapping
    public ResponseEntity<List<TestExecutionResponse>> getTestExecutionsByTestCaseId(@PathVariable UUID testCaseId) {
        // Implementation goes here
        return ResponseEntity.ok(testExecutionService.getTestExecutionsByTestCaseId(testCaseId));
    }
}
