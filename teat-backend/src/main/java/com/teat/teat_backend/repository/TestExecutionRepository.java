package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.TestExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestExecutionRepository extends JpaRepository<TestExecution, UUID> {
    Optional<TestExecution> findByTestCaseId(UUID testCaseId);
    List<TestExecution> findAllByTestCaseId(UUID testCaseId);
}
