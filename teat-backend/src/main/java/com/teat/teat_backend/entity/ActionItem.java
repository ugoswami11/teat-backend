package com.teat.teat_backend.entity;

import com.teat.teat_backend.entity.enums.ActionItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "action_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionItem {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_execution_id", nullable = false)
    private TestExecution testExecution;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionItemStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
