package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestCaseRequest;
import com.teat.teat_backend.dto.response.TestCaseResponse;
import com.teat.teat_backend.entity.TestCase;
import com.teat.teat_backend.entity.TestRun;
import com.teat.teat_backend.exception.BusinessValidationException;
import com.teat.teat_backend.repository.TestCaseRepository;
import com.teat.teat_backend.repository.TestRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestRunRepository testRunRepository;

    @Override
    public TestCaseResponse createTestCase(UUID testRunId, CreateTestCaseRequest request) {
        TestRun testRun = testRunRepository.findById(testRunId)
                .orElseThrow(() -> new BusinessValidationException("TestRun not found"));

        TestCase testCase = TestCase.builder()
                .testRun(testRun)
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        TestCase savedTestCase = testCaseRepository.save(testCase);

        return TestCaseResponse.builder()
                .id(savedTestCase.getId())
                .testRunId(testRunId)
                .title(savedTestCase.getTitle())
                .description(savedTestCase.getDescription())
                .createdAt(savedTestCase.getCreatedAt())
                .build();
    }

    @Override
    public List<TestCaseResponse> getTestCasesByTestRunId(UUID testRunId) {
        return testCaseRepository.findByTestRunId(testRunId).stream()
                .map(testCase -> TestCaseResponse.builder()
                        .id(testCase.getId())
                        .testRunId(testCase.getTestRun().getId())
                        .title(testCase.getTitle())
                        .description(testCase.getDescription())
                        .createdAt(testCase.getCreatedAt())
                        .build())
                .toList();
    }
}
