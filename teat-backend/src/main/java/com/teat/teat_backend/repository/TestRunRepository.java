package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.TestRun;
import com.teat.teat_backend.entity.enums.TestRunStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TestRun entity with support for filtering via specifications.
 */
@Repository
public interface TestRunRepository extends JpaRepository<TestRun, UUID>, JpaSpecificationExecutor<TestRun> {

    Optional<TestRun> findByIdAndIsDeletedFalse(UUID id);

    List<TestRun> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);

    List<TestRun> findByStatusAndIsDeletedFalse(TestRunStatus status);

    List<TestRun> findByCreatedByAndIsDeletedFalse(UUID createdBy);
}
