package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.ActionItem;
import com.teat.teat_backend.entity.enums.ActionItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ActionItem entity with support for filtering via specifications.
 */
@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, UUID>, JpaSpecificationExecutor<ActionItem> {

    Optional<ActionItem> findByIdAndIsDeletedFalse(UUID id);

    List<ActionItem> findByStatusAndIsDeletedFalse(ActionItemStatus status);

    List<ActionItem> findByTestRunTestCaseIdAndIsDeletedFalse(UUID testRunTestCaseId);

    int countByStatusAndIsDeletedFalse(ActionItemStatus status);
}
