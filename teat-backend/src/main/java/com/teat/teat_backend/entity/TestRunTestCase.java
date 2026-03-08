package com.teat.teat_backend.entity;

import com.teat.teat_backend.entity.enums.ExecutionStatus;
import com.teat.teat_backend.entity.enums.ExecutionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
    name = "test_run_test_case",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"test_run_id", "test_case_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"testRun", "testCase", "actionItems"})
public class TestRunTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_run_id", nullable = false)
    private TestRun testRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    @Enumerated(EnumType.STRING)
    @Column(name = "execution_status", length = 50, nullable = false)
    private ExecutionStatus executionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "execution_type", length = 50)
    private ExecutionType executionType;

    @Column(name = "executed_by", length = 100)
    private String executedBy;

    @Column(name = "executed_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime executedAt;

    @Column(columnDefinition = "text", nullable = false)
    private String evidence;

    @Column(name = "created_by", nullable = false, columnDefinition = "uuid")
    private UUID createdBy;

    @Column(name = "updated_by", nullable = false, columnDefinition = "uuid")
    private UUID updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "testRunTestCase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ActionItem> actionItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (executionStatus == null) {
            executionStatus = ExecutionStatus.NOT_EXECUTED;
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }
}
