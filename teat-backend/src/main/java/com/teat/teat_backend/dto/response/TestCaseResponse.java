package com.teat.teat_backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class TestCaseResponse {

    private UUID id;
    private UUID testRunId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
