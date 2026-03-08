package com.teat.teat_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

/**
 * Response DTO for TestStep.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestStepDTO {

    private UUID id;
    private Integer stepOrder;
    private String description;
    private String expectedResult;
    private Integer version;
}
