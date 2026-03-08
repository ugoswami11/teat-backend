package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestCaseRequest;
import com.teat.teat_backend.dto.request.UpdateTestCaseRequest;
import com.teat.teat_backend.dto.response.TestCaseDTO;
import com.teat.teat_backend.entity.TestCase;
import com.teat.teat_backend.exception.OptimisticLockException;
import com.teat.teat_backend.exception.ResourceNotFoundException;
import com.teat.teat_backend.mapper.TestCaseMapper;
import com.teat.teat_backend.repository.TestCaseRepository;
import com.teat.teat_backend.repository.TestStepRepository;
import com.teat.teat_backend.util.AuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for TestCase operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestStepRepository testStepRepository;
    private final TestCaseMapper testCaseMapper;

    @Override
    @Transactional
    public TestCaseDTO createTestCase(CreateTestCaseRequest request) {
        log.info("Creating new test case: {}", request.getName());

        UUID userId = AuthProvider.getCurrentUserId();

        TestCase testCase = TestCase.builder()
                .name(request.getName())
                .objective(request.getObjective())
                .expectedResult(request.getExpectedResult())
                .labels(request.getLabels())
                .createdBy(userId)
                .updatedBy(userId)
                .isDeleted(false)
                .version(0)
                .build();

        TestCase savedTestCase = testCaseRepository.save(testCase);
        log.info("Test case created with ID: {}", savedTestCase.getId());

        return testCaseMapper.toDTO(savedTestCase);
    }

    @Override
    @Transactional(readOnly = true)
    public TestCaseDTO getTestCaseById(UUID id) {
        log.debug("Fetching test case by ID: {}", id);

        TestCase testCase = testCaseRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestCase", "id", id));

        TestCaseDTO dto = testCaseMapper.toDTO(testCase);

        // Fetch and map test steps
        var steps = testStepRepository.findByTestCaseIdAndIsDeletedFalseOrderByStepOrderAsc(id)
                .stream()
                .map(testCaseMapper::toTestStepDTO)
                .collect(Collectors.toList());

        dto.setSteps(steps);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestCaseDTO> getAllTestCases(Pageable pageable, String name, String label, String createdBy) {
        log.debug("Fetching test cases with filters - name: {}, label: {}, createdBy: {}", name, label, createdBy);

        Specification<TestCase> spec = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (label != null && !label.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("labels")), "%" + label.toLowerCase() + "%"));
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

        Page<TestCase> testCases = testCaseRepository.findAll(spec, pageable);
        return testCases.map(testCaseMapper::toDTO);
    }

    @Override
    @Transactional
    public TestCaseDTO updateTestCase(UUID id, UpdateTestCaseRequest request) {
        log.info("Updating test case with ID: {}", id);

        TestCase testCase = testCaseRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestCase", "id", id));

        if (!testCase.getVersion().equals(request.getVersion())) {
            log.warn("Version mismatch for test case ID: {}", id);
            throw new OptimisticLockException("TestCase", id);
        }

        testCase.setName(request.getName());
        testCase.setObjective(request.getObjective());
        testCase.setExpectedResult(request.getExpectedResult());
        testCase.setLabels(request.getLabels());
        testCase.setUpdatedBy(AuthProvider.getCurrentUserId());

        TestCase updatedTestCase = testCaseRepository.save(testCase);
        log.info("Test case updated with ID: {}", id);

        return testCaseMapper.toDTO(updatedTestCase);
    }

    @Override
    @Transactional
    public void deleteTestCase(UUID id) {
        log.info("Soft deleting test case with ID: {}", id);

        TestCase testCase = testCaseRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestCase", "id", id));

        testCase.setIsDeleted(true);
        testCase.setUpdatedBy(AuthProvider.getCurrentUserId());
        testCaseRepository.save(testCase);

        log.info("Test case soft deleted with ID: {}", id);
    }

    @Override
    @Transactional
    public void restoreTestCase(UUID id) {
        log.info("Restoring soft-deleted test case with ID: {}", id);

        TestCase testCase = testCaseRepository.findById(id)
                .filter(TestCase::getIsDeleted)
                .orElseThrow(() -> new ResourceNotFoundException("TestCase", "id", id));

        testCase.setIsDeleted(false);
        testCase.setUpdatedBy(AuthProvider.getCurrentUserId());
        testCaseRepository.save(testCase);

        log.info("Test case restored with ID: {}", id);
    }
}
