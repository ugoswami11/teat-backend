package com.teat.teat_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response DTO for ActionItem with latest resolution summary.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionItemDTO {

    private UUID id;
    private String title;
    private String description;
    private String status;
    private String assignedTo;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer version;
    private ActionItemResolutionSummaryDTO latestResolution;
}
