package com.teat.teat_backend.controller;

import com.teat.teat_backend.dto.request.UpdateTestStepRequest;
import com.teat.teat_backend.dto.response.TestStepDTO;
import com.teat.teat_backend.service.TestStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Test Step operations.
 */
@RestController
@RequestMapping("/api/test-steps")
@RequiredArgsConstructor
@Slf4j
public class TestStepController {

    private final TestStepService testStepService;

    /**
     * Update a test step.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestStepDTO> updateTestStep(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTestStepRequest request) {
        log.info("PUT /test-steps/{} - Updating test step", id);
        TestStepDTO step = testStepService.updateTestStep(id, request);
        return ResponseEntity.ok(step);
    }

    /**
     * Delete a test step.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestStep(@PathVariable UUID id) {
        log.info("DELETE /test-steps/{} - Deleting test step", id);
        testStepService.deleteTestStep(id);
        return ResponseEntity.noContent().build();
    }
}
