package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestRunRequest;
import com.teat.teat_backend.dto.response.TestRunResposne;

import java.util.List;

public interface TestRunService {
    TestRunResposne createTestRun(CreateTestRunRequest request);
    List<TestRunResposne> getAllTestRuns();
}
