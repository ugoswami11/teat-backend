package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestStepRequest;
import com.teat.teat_backend.dto.request.UpdateTestStepRequest;
import com.teat.teat_backend.dto.response.TestStepDTO;
import com.teat.teat_backend.entity.TestCase;
import com.teat.teat_backend.entity.TestStep;
import com.teat.teat_backend.exception.OptimisticLockException;
import com.teat.teat_backend.exception.ResourceNotFoundException;
import com.teat.teat_backend.mapper.TestCaseMapper;
import com.teat.teat_backend.repository.TestCaseRepository;
import com.teat.teat_backend.repository.TestStepRepository;
import com.teat.teat_backend.util.AuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for TestStep operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestStepServiceImpl implements TestStepService {

    private final TestStepRepository testStepRepository;
    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;

    @Override
    @Transactional
    public TestStepDTO createTestStep(UUID testCaseId, CreateTestStepRequest request) {
        log.info("Creating test step for test case ID: {}, order: {}", testCaseId, request.getStepOrder());

        TestCase testCase = testCaseRepository.findByIdAndIsDeletedFalse(testCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("TestCase", "id", testCaseId));

        TestStep testStep = TestStep.builder()
                .testCase(testCase)
                .stepOrder(request.getStepOrder())
                .stepDescription(request.getDescription())
                .expectedResult(request.getExpectedResult())
                .createdBy(AuthProvider.getCurrentUserId())
                .updatedBy(AuthProvider.getCurrentUserId())
                .isDeleted(false)
                .version(0)
                .build();

        TestStep savedTestStep = testStepRepository.save(testStep);
        log.info("Test step created with ID: {}", savedTestStep.getId());

        return testCaseMapper.toTestStepDTO(savedTestStep);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestStepDTO> getTestStepsByTestCaseId(UUID testCaseId) {
        log.debug("Fetching test steps for test case ID: {}", testCaseId);

        // Verify test case exists
        testCaseRepository.findByIdAndIsDeletedFalse(testCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("TestCase", "id", testCaseId));

        return testStepRepository.findByTestCaseIdAndIsDeletedFalseOrderByStepOrderAsc(testCaseId)
                .stream()
                .map(testCaseMapper::toTestStepDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TestStepDTO getTestStepById(UUID id) {
        log.debug("Fetching test step by ID: {}", id);

        TestStep testStep = testStepRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestStep", "id", id));

        return testCaseMapper.toTestStepDTO(testStep);
    }

    @Override
    @Transactional
    public TestStepDTO updateTestStep(UUID id, UpdateTestStepRequest request) {
        log.info("Updating test step with ID: {}", id);

        TestStep testStep = testStepRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestStep", "id", id));

        testStep.setStepOrder(request.getStepOrder());
        if (request.getDescription() != null) {
            testStep.setStepDescription(request.getDescription());
        }
        if (request.getExpectedResult() != null) {
            testStep.setExpectedResult(request.getExpectedResult());
        }
        testStep.setUpdatedBy(AuthProvider.getCurrentUserId());

        TestStep updatedTestStep = testStepRepository.save(testStep);
        log.info("Test step updated with ID: {}", id);

        return testCaseMapper.toTestStepDTO(updatedTestStep);
    }

    @Override
    @Transactional
    public void deleteTestStep(UUID id) {
        log.info("Soft deleting test step with ID: {}", id);

        TestStep testStep = testStepRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestStep", "id", id));

        testStep.setIsDeleted(true);
        testStep.setUpdatedBy(AuthProvider.getCurrentUserId());
        testStepRepository.save(testStep);

        log.info("Test step soft deleted with ID: {}", id);
    }
}
