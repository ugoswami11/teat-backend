package com.teat.teat_backend.controller;

import com.teat.teat_backend.dto.request.CreateActionItemRequest;
import com.teat.teat_backend.dto.response.ActionItemResponse;
import com.teat.teat_backend.entity.enums.ActionItemStatus;
import com.teat.teat_backend.service.ActionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ActionItemController {

    private final ActionItemService actionItemService;

    @PostMapping("/test-executions/{executionId}/action-items")
    public ResponseEntity<ActionItemResponse> createActionItem(@PathVariable UUID executionId,
                                                               @Valid @RequestBody CreateActionItemRequest request) {
        // Implementation goes here
        ActionItemResponse response = actionItemService.createActionItem(executionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/action-items")
    public ResponseEntity<List<ActionItemResponse>> getActionItems(
            @RequestParam Optional<ActionItemStatus> status) {
        // Implementation goes here
        return ResponseEntity.ok(actionItemService.getActionItems(status));
    }
}
