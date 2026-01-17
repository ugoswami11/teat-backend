package com.teat.teat_backend.entity;

import com.teat.teat_backend.entity.enums.ExecutionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_executions", uniqueConstraints = @UniqueConstraint(columnNames = "test_case_id"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestExecution {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "test_case_id", nullable = false, unique = true)
    private TestCase testCase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionStatus status;

    private String evidence;

    @Column(nullable = false)
    private String executedBy;

    @Column(name = "executed_at", insertable = false, updatable = false)
    private LocalDateTime executedAt;

}
