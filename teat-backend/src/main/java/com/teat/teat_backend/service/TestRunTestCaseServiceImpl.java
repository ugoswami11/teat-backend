package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.ExecuteTestCaseRequest;
import com.teat.teat_backend.dto.response.TestExecutionDTO;
import com.teat.teat_backend.entity.TestRunTestCase;
import com.teat.teat_backend.entity.enums.ExecutionStatus;
import com.teat.teat_backend.exception.OptimisticLockException;
import com.teat.teat_backend.exception.ResourceNotFoundException;
import com.teat.teat_backend.repository.TestRunRepository;
import com.teat.teat_backend.repository.TestRunTestCaseRepository;
import com.teat.teat_backend.util.AuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Service implementation for TestRunTestCase (Test Execution) operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestRunTestCaseServiceImpl implements TestRunTestCaseService {

    private final TestRunTestCaseRepository testRunTestCaseRepository;
    private final TestRunRepository testRunRepository;

    @Override
    @Transactional
    public TestExecutionDTO executeTestCase(UUID testRunId, UUID testRunTestCaseId, ExecuteTestCaseRequest request) {
        log.info("Executing test case {} in test run {}", testRunTestCaseId, testRunId);

        // Verify test run exists
        testRunRepository.findByIdAndIsDeletedFalse(testRunId)
                .orElseThrow(() -> new ResourceNotFoundException("TestRun", "id", testRunId));

        TestRunTestCase execution = testRunTestCaseRepository.findByIdAndIsDeletedFalse(testRunTestCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("TestRunTestCase", "id", testRunTestCaseId));

        // Verify the execution belongs to the requested test run
        if (!execution.getTestRun().getId().equals(testRunId)) {
            throw new ResourceNotFoundException("TestRunTestCase", "id", testRunTestCaseId);
        }

        // Check version for optimistic locking
        if (!execution.getVersion().equals(request.getVersion())) {
            log.warn("Version mismatch for test execution ID: {}", testRunTestCaseId);
            throw new OptimisticLockException("TestRunTestCase", testRunTestCaseId);
        }

        // Update execution details
        try {
            execution.setExecutionStatus(ExecutionStatus.valueOf(request.getExecutionStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid execution status: {}", request.getExecutionStatus());
            throw new com.teat.teat_backend.exception.BusinessValidationException("Invalid execution status");
        }

        if (request.getExecutionType() != null && !request.getExecutionType().isBlank()) {
            try {
                execution.setExecutionType(
                        com.teat.teat_backend.entity.enums.ExecutionType.valueOf(request.getExecutionType().toUpperCase())
                );
            } catch (IllegalArgumentException e) {
                log.warn("Invalid execution type: {}", request.getExecutionType());
            }
        }

        execution.setExecutedBy(request.getExecutedBy());
        execution.setExecutedAt(OffsetDateTime.now());
        execution.setEvidence(request.getEvidence());
        execution.setUpdatedBy(AuthProvider.getCurrentUserId());

        TestRunTestCase updatedExecution = testRunTestCaseRepository.save(execution);
        log.info("Test case executed with ID: {}", testRunTestCaseId);

        return mapToDTO(updatedExecution);
    }

    @Override
    @Transactional(readOnly = true)
    public TestExecutionDTO getTestExecutionById(UUID testRunTestCaseId) {
        log.debug("Fetching test execution by ID: {}", testRunTestCaseId);

        TestRunTestCase execution = testRunTestCaseRepository.findByIdAndIsDeletedFalse(testRunTestCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("TestRunTestCase", "id", testRunTestCaseId));

        return mapToDTO(execution);
    }

    private TestExecutionDTO mapToDTO(TestRunTestCase execution) {
        return TestExecutionDTO.builder()
                .id(execution.getId())
                .executionStatus(execution.getExecutionStatus().name())
                .executionType(execution.getExecutionType() != null ? execution.getExecutionType().name() : null)
                .executedBy(execution.getExecutedBy())
                .executedAt(execution.getExecutedAt())
                .evidence(execution.getEvidence())
                .version(execution.getVersion())
                .build();
    }
}
