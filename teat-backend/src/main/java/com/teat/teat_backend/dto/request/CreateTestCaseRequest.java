package com.teat.teat_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CreateTestCaseRequest {

    @NotBlank(message = "Title is required")
    private String title;
    private String description;
}
