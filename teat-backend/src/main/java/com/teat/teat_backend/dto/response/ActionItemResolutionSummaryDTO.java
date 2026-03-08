package com.teat.teat_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Summary DTO for ActionItemResolution - used in ActionItemDTO.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionItemResolutionSummaryDTO {

    private UUID id;
    private String resolutionDetails;
    private String evidenceLink;
    private String resolvedBy;
    private OffsetDateTime resolvedAt;
}
