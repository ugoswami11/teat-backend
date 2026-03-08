package com.teat.teat_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for creating a test case.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTestCaseRequest {

    @NotBlank(message = "Test case name is required")
    @Size(max = 255, message = "Test case name must not exceed 255 characters")
    private String name;

    @Size(max = 2000, message = "Objective must not exceed 2000 characters")
    private String objective;

    @Size(max = 2000, message = "Expected result must not exceed 2000 characters")
    private String expectedResult;

    @Size(max = 255, message = "Labels must not exceed 255 characters")
    private String labels;
}
