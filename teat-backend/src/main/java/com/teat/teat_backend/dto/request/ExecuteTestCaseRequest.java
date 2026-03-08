package com.teat.teat_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for executing a test case within a test run.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteTestCaseRequest {

    @NotBlank(message = "Execution status is required")
    private String executionStatus;

    private String executionType;

    @Size(max = 100, message = "Executed by must not exceed 100 characters")
    private String executedBy;

    @NotBlank(message = "Evidence is required")
    @Size(max = 5000, message = "Evidence must not exceed 5000 characters")
    private String evidence;

    @NotNull(message = "Version is required for optimistic locking")
    private Integer version;
}
