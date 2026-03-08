package com.teat.teat_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

/**
 * Summary DTO for TestCase - used in TestRunDTO to avoid deep nesting.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCaseSummaryDTO {

    private UUID id;
    private String name;
    private String objective;
    private String expectedResult;
    private String labels;
    private Integer version;
}
