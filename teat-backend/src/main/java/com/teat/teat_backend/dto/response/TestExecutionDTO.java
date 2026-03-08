package com.teat.teat_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response DTO for TestExecution (TestRunTestCase).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestExecutionDTO {

    private UUID id;
    private String executionStatus;
    private String executionType;
    private String executedBy;
    private OffsetDateTime executedAt;
    private String evidence;
    private Integer version;
}
