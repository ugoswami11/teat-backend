package com.teat.teat_backend.controller;

import com.teat.teat_backend.dto.request.CreateTestRunRequest;
import com.teat.teat_backend.dto.response.TestRunResposne;
import com.teat.teat_backend.service.TestRunService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test-runs")
@RequiredArgsConstructor
public class TestRunController {

    private final TestRunService testRunService;

    @PostMapping
    public ResponseEntity<TestRunResposne> createTestRun(
            @Valid @RequestBody CreateTestRunRequest request) {

        TestRunResposne response = testRunService.createTestRun(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TestRunResposne>> getAllTestRuns() {
        return ResponseEntity.ok(testRunService.getAllTestRuns());
    }
}
