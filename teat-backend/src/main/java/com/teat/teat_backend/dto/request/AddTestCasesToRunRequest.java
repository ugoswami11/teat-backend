package com.teat.teat_backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO for adding test cases to a test run.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTestCasesToRunRequest {

    @NotEmpty(message = "At least one test case ID is required")
    private List<UUID> testCaseIds;
}
