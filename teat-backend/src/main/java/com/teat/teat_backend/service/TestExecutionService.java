package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestExecutionRequest;
import com.teat.teat_backend.dto.response.TestExecutionResponse;

import java.util.List;
import java.util.UUID;

public interface TestExecutionService {

    TestExecutionResponse executeTestCase(UUID testCaseId, CreateTestExecutionRequest request);
    List<TestExecutionResponse> getTestExecutionsByTestCaseId(UUID testCaseId);
}
