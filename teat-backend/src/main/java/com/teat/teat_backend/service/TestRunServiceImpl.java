package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateTestRunRequest;
import com.teat.teat_backend.dto.response.TestRunResposne;
import com.teat.teat_backend.entity.TestRun;
import com.teat.teat_backend.repository.TestRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestRunServiceImpl implements TestRunService {

    private final TestRunRepository testRunRepository;


    @Override
    public TestRunResposne createTestRun(CreateTestRunRequest request) {
        TestRun testRun = TestRun.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(request.getCreatedBy())
                .build();

        TestRun savedTestRun = testRunRepository.save(testRun);

        return TestRunResposne.builder()
                .id(savedTestRun.getId())
                .name(savedTestRun.getName())
                .description(savedTestRun.getDescription())
                .createdBy(savedTestRun.getCreatedBy())
                .createdAt(savedTestRun.getCreatedAt())
                .build();
    }

    @Override
    public List<TestRunResposne> getAllTestRuns() {
        return testRunRepository.findAll().stream()
                .map(testRun -> TestRunResposne.builder()
                        .id(testRun.getId())
                        .name(testRun.getName())
                        .description(testRun.getDescription())
                        .createdBy(testRun.getCreatedBy())
                        .createdAt(testRun.getCreatedAt())
                        .build())
                .toList();
    }

}
