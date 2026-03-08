package com.teat.teat_backend.controller;

import com.teat.teat_backend.dto.request.ResolveActionItemRequest;
import com.teat.teat_backend.dto.request.UpdateActionItemRequest;
import com.teat.teat_backend.dto.response.ActionItemDTO;
import com.teat.teat_backend.dto.response.PageResponse;
import com.teat.teat_backend.service.ActionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Action Item operations.
 */
@RestController
@RequestMapping("/api/action-items")
@RequiredArgsConstructor
@Slf4j
public class ActionItemController {

    private final ActionItemService actionItemService;

    /**
     * Get paginated list of action items.
     */
    @GetMapping
    public ResponseEntity<PageResponse<ActionItemDTO>> getAllActionItems(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /action-items - Fetching paginated action items");
        Page<ActionItemDTO> actionItems = actionItemService.getAllActionItems(pageable);
        return ResponseEntity.ok(PageResponse.of(actionItems));
    }

    /**
     * Get an action item by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActionItemDTO> getActionItemById(@PathVariable UUID id) {
        log.info("GET /action-items/{} - Fetching action item", id);
        ActionItemDTO actionItem = actionItemService.getActionItemById(id);
        return ResponseEntity.ok(actionItem);
    }

    /**
     * Update an action item.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActionItemDTO> updateActionItem(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateActionItemRequest request) {
        log.info("PUT /action-items/{} - Updating action item", id);
        ActionItemDTO actionItem = actionItemService.updateActionItem(id, request);
        return ResponseEntity.ok(actionItem);
    }

    /**
     * Resolve an action item.
     */
    @PostMapping("/{id}/resolve")
    public ResponseEntity<ActionItemDTO> resolveActionItem(
            @PathVariable UUID id,
            @Valid @RequestBody ResolveActionItemRequest request) {
        log.info("POST /action-items/{}/resolve - Resolving action item", id);
        ActionItemDTO actionItem = actionItemService.resolveActionItem(id, request);
        return ResponseEntity.ok(actionItem);
    }

    /**
     * Delete (soft delete) an action item.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActionItem(@PathVariable UUID id) {
        log.info("DELETE /action-items/{} - Soft deleting action item", id);
        actionItemService.deleteActionItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restore a soft-deleted action item.
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreActionItem(@PathVariable UUID id) {
        log.info("PATCH /action-items/{}/restore - Restoring action item", id);
        actionItemService.restoreActionItem(id);
        return ResponseEntity.noContent().build();
    }
}
