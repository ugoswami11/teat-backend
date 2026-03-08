package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TestCase entity with support for filtering via specifications.
 */
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, UUID>, JpaSpecificationExecutor<TestCase> {

    Optional<TestCase> findByIdAndIsDeletedFalse(UUID id);

    List<TestCase> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);

    List<TestCase> findByLabelsContainingIgnoreCaseAndIsDeletedFalse(String label);

    List<TestCase> findByCreatedByAndIsDeletedFalse(UUID createdBy);
}
