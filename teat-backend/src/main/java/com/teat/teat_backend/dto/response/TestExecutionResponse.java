package com.teat.teat_backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class TestExecutionResponse {

    private UUID id;
    private UUID testCaseId;
    private String executedBy;
    private String status;
    private String evidence;
    private LocalDateTime executedAt;
}
