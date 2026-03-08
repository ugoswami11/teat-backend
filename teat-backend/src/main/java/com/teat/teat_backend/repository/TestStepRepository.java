package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.TestStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TestStep entity.
 */
@Repository
public interface TestStepRepository extends JpaRepository<TestStep, UUID> {

    Optional<TestStep> findByIdAndIsDeletedFalse(UUID id);

    List<TestStep> findByTestCaseIdAndIsDeletedFalseOrderByStepOrderAsc(UUID testCaseId);
}
