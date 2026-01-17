package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestCaseRequest;
import com.teat.teat_backend.dto.response.TestCaseResponse;

import java.util.List;
import java.util.UUID;

public interface TestCaseService {

    TestCaseResponse createTestCase(UUID testRunId, CreateTestCaseRequest request);
    List<TestCaseResponse> getTestCasesByTestRunId(UUID testRunId);
}
