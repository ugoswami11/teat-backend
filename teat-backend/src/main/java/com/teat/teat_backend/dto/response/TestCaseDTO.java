package com.teat.teat_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for TestCase with nested test steps.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCaseDTO {

    private UUID id;
    private String name;
    private String objective;
    private String expectedResult;
    private String labels;
    private String createdBy;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer version;
    private List<TestStepDTO> steps;
}
