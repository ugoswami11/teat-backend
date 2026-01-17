package com.teat.teat_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTestRunRequest {

    @NotBlank(message = "Test run name is required")
    @Size(max=255, message = "Test run name must not exceed 255 characters")
    private String name;

    @Size(max=2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Size(max=255, message = "Created by must not exceed 255 characters")
    private String createdBy;
}
