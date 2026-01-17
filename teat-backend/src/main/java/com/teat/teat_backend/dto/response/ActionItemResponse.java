package com.teat.teat_backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ActionItemResponse {

    private UUID id;
    private UUID executionId;
    private String status;
    private String description;
    private LocalDateTime createdAt;
}
