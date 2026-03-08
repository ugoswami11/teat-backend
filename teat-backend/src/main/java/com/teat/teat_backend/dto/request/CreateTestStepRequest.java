package com.teat.teat_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for creating a test step.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTestStepRequest {

    @NotNull(message = "Step order is required")
    private Integer stepOrder;

    @NotBlank(message = "Step description is required")
    @Size(max = 2000, message = "Step description must not exceed 2000 characters")
    private String description;

    @Size(max = 2000, message = "Expected result must not exceed 2000 characters")
    private String expectedResult;
}
