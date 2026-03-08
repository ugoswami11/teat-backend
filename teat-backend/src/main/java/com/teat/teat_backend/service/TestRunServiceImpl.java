package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestRunRequest;
import com.teat.teat_backend.dto.request.UpdateTestRunRequest;
import com.teat.teat_backend.dto.response.AddTestCasesToRunResponseDTO;
import com.teat.teat_backend.dto.response.TestRunDTO;
import com.teat.teat_backend.entity.TestCase;
import com.teat.teat_backend.entity.TestRun;
import com.teat.teat_backend.entity.TestRunTestCase;
import com.teat.teat_backend.entity.enums.ExecutionStatus;
import com.teat.teat_backend.entity.enums.TestRunStatus;
import com.teat.teat_backend.exception.BusinessValidationException;
import com.teat.teat_backend.exception.ResourceNotFoundException;
import com.teat.teat_backend.mapper.TestRunMapper;
import com.teat.teat_backend.repository.TestCaseRepository;
import com.teat.teat_backend.repository.TestRunRepository;
import com.teat.teat_backend.repository.TestRunTestCaseRepository;
import com.teat.teat_backend.util.AuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for TestRun operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestRunServiceImpl implements TestRunService {

    private final TestRunRepository testRunRepository;
    private final TestCaseRepository testCaseRepository;
    private final TestRunTestCaseRepository testRunTestCaseRepository;
    private final TestRunMapper testRunMapper;

    @Override
    @Transactional
    public TestRunDTO createTestRun(CreateTestRunRequest request) {
        log.info("Creating new test run: {}", request.getName());

        UUID userId = AuthProvider.getCurrentUserId();

        TestRun testRun = TestRun.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(TestRunStatus.CREATED)
                .createdBy(userId)
                .updatedBy(userId)
                .isDeleted(false)
                .version(0)
                .build();

        TestRun savedTestRun = testRunRepository.save(testRun);
        log.info("Test run created with ID: {}", savedTestRun.getId());

        return testRunMapper.toDTO(savedTestRun);
    }

    @Override
    @Transactional(readOnly = true)
    public TestRunDTO getTestRunById(UUID id) {
        log.debug("Fetching test run by ID: {}", id);

        TestRun testRun = testRunRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestRun", "id", id));

        return testRunMapper.toDTO(testRun);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestRunDTO> getAllTestRuns(Pageable pageable, String name, String status, String createdBy) {
        log.debug("Fetching test runs with filters - name: {}, status: {}, createdBy: {}", name, status, createdBy);

        Specification<TestRun> spec = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (status != null && !status.isBlank()) {
                try {
                    TestRunStatus testRunStatus = TestRunStatus.valueOf(status.toUpperCase());
                    predicates.add(cb.equal(root.get("status"), testRunStatus));
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid status filter: {}", status);
                }
            }

            if (createdBy != null && !createdBy.isBlank()) {
                try {
                    UUID createdByUUID = UUID.fromString(createdBy);
                    predicates.add(cb.equal(root.get("createdBy"), createdByUUID));
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid UUID format for createdBy: {}", createdBy);
                }
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        Page<TestRun> testRuns = testRunRepository.findAll(spec, pageable);
        return testRuns.map(testRunMapper::toDTO);
    }

    @Override
    @Transactional
    public TestRunDTO updateTestRun(UUID id, UpdateTestRunRequest request) {
        log.info("Updating test run with ID: {}", id);

        TestRun testRun = testRunRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestRun", "id", id));

        if (!testRun.getVersion().equals(request.getVersion())) {
            log.warn("Version mismatch for test run ID: {}", id);
            throw new com.teat.teat_backend.exception.OptimisticLockException("TestRun", id);
        }

        testRun.setName(request.getName());
        testRun.setDescription(request.getDescription());
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            try {
                testRun.setStatus(TestRunStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status value: {}", request.getStatus());
            }
        }
        testRun.setUpdatedBy(AuthProvider.getCurrentUserId());

        TestRun updatedTestRun = testRunRepository.save(testRun);
        log.info("Test run updated with ID: {}", id);

        return testRunMapper.toDTO(updatedTestRun);
    }

    @Override
    @Transactional
    public void deleteTestRun(UUID id) {
        log.info("Soft deleting test run with ID: {}", id);

        TestRun testRun = testRunRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestRun", "id", id));

        testRun.setIsDeleted(true);
        testRun.setUpdatedBy(AuthProvider.getCurrentUserId());
        testRunRepository.save(testRun);

        log.info("Test run soft deleted with ID: {}", id);
    }

    @Override
    @Transactional
    public void restoreTestRun(UUID id) {
        log.info("Restoring soft-deleted test run with ID: {}", id);

        TestRun testRun = testRunRepository.findById(id)
                .filter(TestRun::getIsDeleted)
                .orElseThrow(() -> new ResourceNotFoundException("TestRun", "id", id));

        testRun.setIsDeleted(false);
        testRun.setUpdatedBy(AuthProvider.getCurrentUserId());
        testRunRepository.save(testRun);

        log.info("Test run restored with ID: {}", id);
    }

    @Override
    @Transactional
    public AddTestCasesToRunResponseDTO addTestCasesToRun(UUID testRunId, List<UUID> testCaseIds) {
        log.info("Adding {} test cases to test run ID: {}", testCaseIds.size(), testRunId);

        if (testCaseIds.isEmpty()) {
            throw new BusinessValidationException("At least one test case ID is required");
        }

        TestRun testRun = testRunRepository.findByIdAndIsDeletedFalse(testRunId)
                .orElseThrow(() -> new ResourceNotFoundException("TestRun", "id", testRunId));

        UUID userId = AuthProvider.getCurrentUserId();
        var addedExecutions = new java.util.ArrayList<AddTestCasesToRunResponseDTO.TestRunTestCaseSummaryDTO>();

        for (UUID testCaseId : testCaseIds) {
            TestCase testCase = testCaseRepository.findByIdAndIsDeletedFalse(testCaseId)
                    .orElseThrow(() -> new ResourceNotFoundException("TestCase", "id", testCaseId));

            // Check if execution already exists
            if (testRunTestCaseRepository.findByTestRunIdAndTestCaseIdAndIsDeletedFalse(testRunId, testCaseId).isPresent()) {
                log.warn("Test case ID {} already added to test run ID {}", testCaseId, testRunId);
                continue;
            }

            TestRunTestCase execution = TestRunTestCase.builder()
                    .testRun(testRun)
                    .testCase(testCase)
                    .executionStatus(ExecutionStatus.NOT_EXECUTED)
                    .evidence("") // Will be set when execution happens
                    .createdBy(userId)
                    .updatedBy(userId)
                    .isDeleted(false)
                    .version(0)
                    .build();

            TestRunTestCase savedExecution = testRunTestCaseRepository.save(execution);

            addedExecutions.add(AddTestCasesToRunResponseDTO.TestRunTestCaseSummaryDTO.builder()
                    .testRunTestCaseId(savedExecution.getId())
                    .executionStatus(savedExecution.getExecutionStatus().name())
                    .build());

            log.debug("Test case {} added to test run {}", testCaseId, testRunId);
        }

        return AddTestCasesToRunResponseDTO.builder()
                .addedExecutions(addedExecutions)
                .build();
    }
}
