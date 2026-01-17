package com.teat.teat_backend.dto.request;

import com.teat.teat_backend.entity.enums.ExecutionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTestExecutionRequest {

    @NotNull
    private ExecutionStatus status;

    private String evidence;

    @NotBlank
    private String executedBy;
}
