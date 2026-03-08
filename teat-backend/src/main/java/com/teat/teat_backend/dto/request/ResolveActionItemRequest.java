package com.teat.teat_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for resolving an action item.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolveActionItemRequest {

    @NotBlank(message = "Resolution details are required")
    @Size(max = 2000, message = "Resolution details must not exceed 2000 characters")
    private String resolutionDetails;

    @Size(max = 500, message = "Evidence link must not exceed 500 characters")
    private String evidenceLink;

    @NotBlank(message = "Resolved by is required")
    @Size(max = 100, message = "Resolved by must not exceed 100 characters")
    private String resolvedBy;
}
