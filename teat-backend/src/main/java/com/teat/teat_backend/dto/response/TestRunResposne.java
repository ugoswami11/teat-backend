package com.teat.teat_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TestRunResposne {

    private UUID id;
    private String name;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
}
