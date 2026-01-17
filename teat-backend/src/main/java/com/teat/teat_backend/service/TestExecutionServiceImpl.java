package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestExecutionRequest;
import com.teat.teat_backend.dto.response.TestExecutionResponse;
import com.teat.teat_backend.entity.TestCase;
import com.teat.teat_backend.entity.TestExecution;
import com.teat.teat_backend.repository.TestCaseRepository;
import com.teat.teat_backend.repository.TestExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestExecutionServiceImpl implements TestExecutionService {

    private final TestExecutionRepository testExecutionRepository;
    private final TestCaseRepository testCaseRepository;

    @Override
    public TestExecutionResponse executeTestCase(UUID testCaseId, CreateTestExecutionRequest request) {

        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new RuntimeException("TestCase not found"));

        if(testExecutionRepository.findByTestCaseId(testCaseId).isPresent()) {
            throw new RuntimeException("TestCase already executed");
        }

        TestExecution testExecution = TestExecution.builder()
                .testCase(testCase)
                .executedBy(request.getExecutedBy())
                .status(request.getStatus())
                .evidence(request.getEvidence())
                .build();

        TestExecution savedTestExecution = testExecutionRepository.save(testExecution);

        return TestExecutionResponse.builder()
                .id(savedTestExecution.getId())
                .testCaseId(testCaseId)
                .executedBy(savedTestExecution.getExecutedBy())
                .status(savedTestExecution.getStatus().toString())
                .evidence(savedTestExecution.getEvidence())
                .executedAt(savedTestExecution.getExecutedAt())
                .build();
    }

    @Override
    public List<TestExecutionResponse> getTestExecutionsByTestCaseId(UUID testCaseId) {
        return testExecutionRepository.findAllByTestCaseId(testCaseId).stream()
                .map(testExecution -> TestExecutionResponse.builder()
                        .id(testExecution.getId())
                        .testCaseId(testCaseId)
                        .executedBy(testExecution.getExecutedBy())
                        .status(testExecution.getStatus().toString())
                        .evidence(testExecution.getEvidence())
                        .executedAt(testExecution.getExecutedAt())
                        .build())
                .toList();
    }
}
