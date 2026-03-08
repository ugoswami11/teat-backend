package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestExecutionRequest;
import com.teat.teat_backend.dto.response.TestExecutionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * DEPRECATED: This service is kept for backward compatibility only.
 * Use {@link TestRunTestCaseService} and {@link TestRunTestCaseServiceImpl} instead.
 * 
 * The new design uses TestRunTestCase entity which properly tracks test execution
 * within a specific test run, rather than just linking TestCase to TestExecution.
 */
@Service
@RequiredArgsConstructor
@Deprecated(since = "2.0.0", forRemoval = true)
public class TestExecutionServiceImpl implements TestExecutionService {

    @Override
    public TestExecutionResponse executeTestCase(UUID testCaseId, CreateTestExecutionRequest request) {
        throw new UnsupportedOperationException("Use TestRunTestCaseService instead");
    }

    @Override
    public List<TestExecutionResponse> getTestExecutionsByTestCaseId(UUID testCaseId) {
        throw new UnsupportedOperationException("Use TestRunTestCaseService instead");
    }
}
