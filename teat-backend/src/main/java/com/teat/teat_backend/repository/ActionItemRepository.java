package com.teat.teat_backend.repository;

import com.teat.teat_backend.entity.ActionItem;
import com.teat.teat_backend.entity.enums.ActionItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActionItemRepository extends JpaRepository<ActionItem, UUID> {
    List<ActionItem> findByStatus(ActionItemStatus status);
}
