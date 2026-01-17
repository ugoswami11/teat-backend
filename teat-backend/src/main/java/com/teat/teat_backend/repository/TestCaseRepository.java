package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestCaseRepository extends JpaRepository<TestCase, UUID> {
    List<TestCase> findByTestRunId(UUID testRunId);
}
