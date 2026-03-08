package com.teat.teat_backend.service;

import com.teat.teat_backend.dto.request.UpdateActionItemRequest;
import com.teat.teat_backend.dto.request.ResolveActionItemRequest;
import com.teat.teat_backend.dto.response.ActionItemDTO;
import com.teat.teat_backend.entity.ActionItem;
import com.teat.teat_backend.entity.ActionItemResolution;
import com.teat.teat_backend.entity.TestRunTestCase;
import com.teat.teat_backend.entity.enums.ActionItemStatus;
import com.teat.teat_backend.exception.OptimisticLockException;
import com.teat.teat_backend.exception.ResourceNotFoundException;
import com.teat.teat_backend.mapper.ActionItemMapper;
import com.teat.teat_backend.repository.ActionItemRepository;
import com.teat.teat_backend.repository.ActionItemResolutionRepository;
import com.teat.teat_backend.repository.TestRunTestCaseRepository;
import com.teat.teat_backend.util.AuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Service implementation for ActionItem operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActionItemServiceImpl implements ActionItemService {

    private final ActionItemRepository actionItemRepository;
    private final TestRunTestCaseRepository testRunTestCaseRepository;
    private final ActionItemResolutionRepository resolutionRepository;
    private final ActionItemMapper actionItemMapper;

    @Override
    @Transactional
    public ActionItemDTO createActionItem(UUID testRunTestCaseId, String title, String description) {
        log.info("Creating action item for test execution ID: {}", testRunTestCaseId);

        TestRunTestCase execution = testRunTestCaseRepository.findByIdAndIsDeletedFalse(testRunTestCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("TestRunTestCase", "id", testRunTestCaseId));

        ActionItem actionItem = ActionItem.builder()
                .testRunTestCase(execution)
                .title(title)
                .description(description)
                .status(ActionItemStatus.OPEN)
                .createdBy(AuthProvider.getCurrentUserId())
                .updatedBy(AuthProvider.getCurrentUserId())
                .isDeleted(false)
                .version(0)
                .build();

        ActionItem savedActionItem = actionItemRepository.save(actionItem);
        log.info("Action item created with ID: {}", savedActionItem.getId());

        return actionItemMapper.toDTO(savedActionItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ActionItemDTO getActionItemById(UUID id) {
        log.debug("Fetching action item by ID: {}", id);

        ActionItem actionItem = actionItemRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActionItem", "id", id));

        ActionItemDTO dto = actionItemMapper.toDTO(actionItem);

        // Fetch latest resolution if exists
        resolutionRepository.findFirstByActionItemIdAndIsDeletedFalseOrderByResolvedAtDesc(id)
                .ifPresent(resolution -> dto.setLatestResolution(actionItemMapper.toResolutionSummaryDTO(resolution)));

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActionItemDTO> getAllActionItems(Pageable pageable) {
        log.debug("Fetching paginated action items");

        Specification<ActionItem> spec = (root, query, cb) ->
            cb.equal(root.get("isDeleted"), false);

        Page<ActionItem> actionItems = actionItemRepository.findAll(spec, pageable);
        return actionItems.map(actionItem -> {
            ActionItemDTO dto = actionItemMapper.toDTO(actionItem);
            resolutionRepository.findFirstByActionItemIdAndIsDeletedFalseOrderByResolvedAtDesc(actionItem.getId())
                    .ifPresent(resolution -> dto.setLatestResolution(actionItemMapper.toResolutionSummaryDTO(resolution)));
            return dto;
        });
    }

    @Override
    @Transactional
    public ActionItemDTO updateActionItem(UUID id, UpdateActionItemRequest request) {
        log.info("Updating action item with ID: {}", id);

        ActionItem actionItem = actionItemRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActionItem", "id", id));

        if (!actionItem.getVersion().equals(request.getVersion())) {
            log.warn("Version mismatch for action item ID: {}", id);
            throw new OptimisticLockException("ActionItem", id);
        }

        if (request.getAssignedTo() != null && !request.getAssignedTo().isBlank()) {
            actionItem.setAssignedTo(UUID.fromString(request.getAssignedTo()));
        }

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            try {
                actionItem.setStatus(ActionItemStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status value: {}", request.getStatus());
                throw new com.teat.teat_backend.exception.BusinessValidationException("Invalid action item status");
            }
        }

        actionItem.setUpdatedBy(AuthProvider.getCurrentUserId());

        ActionItem updatedActionItem = actionItemRepository.save(actionItem);
        log.info("Action item updated with ID: {}", id);

        ActionItemDTO dto = actionItemMapper.toDTO(updatedActionItem);
        resolutionRepository.findFirstByActionItemIdAndIsDeletedFalseOrderByResolvedAtDesc(id)
                .ifPresent(resolution -> dto.setLatestResolution(actionItemMapper.toResolutionSummaryDTO(resolution)));

        return dto;
    }

    @Override
    @Transactional
    public ActionItemDTO resolveActionItem(UUID id, ResolveActionItemRequest request) {
        log.info("Resolving action item with ID: {}", id);

        ActionItem actionItem = actionItemRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActionItem", "id", id));

        // Create resolution record
        ActionItemResolution resolution = ActionItemResolution.builder()
                .actionItem(actionItem)
                .resolutionDetails(request.getResolutionDetails())
                .evidenceLink(request.getEvidenceLink())
                .resolvedBy(UUID.fromString(request.getResolvedBy()))
                .resolvedAt(OffsetDateTime.now())
                .createdBy(AuthProvider.getCurrentUserId())
                .updatedBy(AuthProvider.getCurrentUserId())
                .isDeleted(false)
                .version(0)
                .build();

        ActionItemResolution savedResolution = resolutionRepository.save(resolution);
        log.info("Action item resolution created with ID: {}", savedResolution.getId());

        ActionItemDTO dto = actionItemMapper.toDTO(actionItem);
        dto.setLatestResolution(actionItemMapper.toResolutionSummaryDTO(savedResolution));

        return dto;
    }

    @Override
    @Transactional
    public void deleteActionItem(UUID id) {
        log.info("Soft deleting action item with ID: {}", id);

        ActionItem actionItem = actionItemRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActionItem", "id", id));

        actionItem.setIsDeleted(true);
        actionItem.setUpdatedBy(AuthProvider.getCurrentUserId());
        actionItemRepository.save(actionItem);

        log.info("Action item soft deleted with ID: {}", id);
    }

    @Override
    @Transactional
    public void restoreActionItem(UUID id) {
        log.info("Restoring soft-deleted action item with ID: {}", id);

        ActionItem actionItem = actionItemRepository.findById(id)
                .filter(ActionItem::getIsDeleted)
                .orElseThrow(() -> new ResourceNotFoundException("ActionItem", "id", id));

        actionItem.setIsDeleted(false);
        actionItem.setUpdatedBy(AuthProvider.getCurrentUserId());
        actionItemRepository.save(actionItem);

        log.info("Action item restored with ID: {}", id);
    }
}
