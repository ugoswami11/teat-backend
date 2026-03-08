package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.TestRunTestCase;
import com.teat.teat_backend.entity.enums.ExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TestRunTestCase entity (execution instances).
 */
@Repository
public interface TestRunTestCaseRepository extends JpaRepository<TestRunTestCase, UUID> {

    Optional<TestRunTestCase> findByIdAndIsDeletedFalse(UUID id);

    List<TestRunTestCase> findByTestRunIdAndIsDeletedFalse(UUID testRunId);

    List<TestRunTestCase> findByTestCaseIdAndIsDeletedFalse(UUID testCaseId);

    List<TestRunTestCase> findByExecutionStatusAndIsDeletedFalse(ExecutionStatus status);

    Optional<TestRunTestCase> findByTestRunIdAndTestCaseIdAndIsDeletedFalse(UUID testRunId, UUID testCaseId);

    int countByTestRunIdAndExecutionStatusAndIsDeletedFalse(UUID testRunId, ExecutionStatus status);
}
