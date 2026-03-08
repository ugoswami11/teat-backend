package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.ActionItemResolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ActionItemResolution entity.
 */
@Repository
public interface ActionItemResolutionRepository extends JpaRepository<ActionItemResolution, UUID> {

    Optional<ActionItemResolution> findByIdAndIsDeletedFalse(UUID id);

    List<ActionItemResolution> findByActionItemIdAndIsDeletedFalseOrderByResolvedAtDesc(UUID actionItemId);

    Optional<ActionItemResolution> findFirstByActionItemIdAndIsDeletedFalseOrderByResolvedAtDesc(UUID actionItemId);
}
