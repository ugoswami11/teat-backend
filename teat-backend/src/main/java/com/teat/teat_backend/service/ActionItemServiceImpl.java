package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.CreateActionItemRequest;
import com.teat.teat_backend.dto.response.ActionItemResponse;
import com.teat.teat_backend.entity.ActionItem;
import com.teat.teat_backend.entity.TestExecution;
import com.teat.teat_backend.entity.enums.ActionItemStatus;
import com.teat.teat_backend.entity.enums.ExecutionStatus;
import com.teat.teat_backend.exception.BusinessValidationException;
import com.teat.teat_backend.repository.ActionItemRepository;
import com.teat.teat_backend.repository.TestExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActionItemServiceImpl implements ActionItemService {

    private final ActionItemRepository actionItemRepository;
    private final TestExecutionRepository testExecutionRepository;

    @Override
    public ActionItemResponse createActionItem(UUID executionId, CreateActionItemRequest request) {
        TestExecution testExecution = testExecutionRepository.findById(executionId)
                .orElseThrow(() -> new IllegalArgumentException("TestExecution not found"));

        if(testExecution.getStatus()!= ExecutionStatus.FAILED){
            throw new BusinessValidationException("Cannot create action item for non-failed execution");
        }

        ActionItem actionItem = ActionItem.builder()
                .testExecution(testExecution)
                .description(request.getDescription())
                .status(ActionItemStatus.OPEN)
                .build();

        ActionItem savedActionItem = actionItemRepository.save(actionItem);

        return ActionItemResponse.builder()
                .id(savedActionItem.getId())
                .executionId(executionId)
                .status(savedActionItem.getStatus().name())
                .description(savedActionItem.getDescription())
                .createdAt(savedActionItem.getCreatedAt())
                .build();
    }

    @Override
    public List<ActionItemResponse> getActionItems(Optional<ActionItemStatus> status) {

        List<ActionItem> actionItems = status
                .map(actionItemRepository::findByStatus)
                .orElseGet(actionItemRepository::findAll);

        return actionItems.stream()
                .map(item-> ActionItemResponse.builder()
                        .id(item.getId())
                        .executionId(item.getTestExecution().getId())
                        .status(item.getStatus().name())
                        .description(item.getDescription())
                        .createdAt(item.getCreatedAt())
                        .build())
                .toList();
    }
}
