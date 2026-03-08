package com.teat.teat_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Response DTO containing test run test case IDs and execution statuses
 * when test cases are added to a test run.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddTestCasesToRunResponseDTO {

    private List<TestRunTestCaseSummaryDTO> addedExecutions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TestRunTestCaseSummaryDTO {
        private UUID testRunTestCaseId;
        private String executionStatus;
    }
}
