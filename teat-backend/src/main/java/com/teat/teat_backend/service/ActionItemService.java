package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.UpdateActionItemRequest;
import com.teat.teat_backend.dto.request.ResolveActionItemRequest;
import com.teat.teat_backend.dto.response.ActionItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface for ActionItem operations.
 */
public interface ActionItemService {

    /**
     * Create a new action item for a failed test execution.
     */
    ActionItemDTO createActionItem(UUID testRunTestCaseId, String title, String description);

    /**
     * Get an action item by ID.
     */
    ActionItemDTO getActionItemById(UUID id);

    /**
     * Get paginated list of action items with optional filtering.
     */
    Page<ActionItemDTO> getAllActionItems(Pageable pageable);

    /**
     * Update an action item.
     */
    ActionItemDTO updateActionItem(UUID id, UpdateActionItemRequest request);

    /**
     * Resolve an action item (record resolution details).
     */
    ActionItemDTO resolveActionItem(UUID id, ResolveActionItemRequest request);

    /**
     * Soft delete an action item.
     */
    void deleteActionItem(UUID id);

    /**
     * Restore a soft-deleted action item.
     */
    void restoreActionItem(UUID id);
}
