package com.teat.teat_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for updating an action item.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateActionItemRequest {

    @Size(max = 100, message = "Assigned to must not exceed 100 characters")
    private String assignedTo;

    @Size(max = 50, message = "Status must not exceed 50 characters")
    private String status;

    @NotNull(message = "Version is required for optimistic locking")
    private Integer version;
}
