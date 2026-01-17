package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateActionItemRequest;
import com.teat.teat_backend.dto.response.ActionItemResponse;
import com.teat.teat_backend.entity.enums.ActionItemStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActionItemService {

    ActionItemResponse createActionItem(UUID executionId, CreateActionItemRequest request);

    List<ActionItemResponse> getActionItems(Optional<ActionItemStatus> status);
}
