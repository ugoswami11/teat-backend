package com.teat.teat_backend.entity;

import com.teat.teat_backend.entity.enums.TestRunStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "test_runs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "testRunTestCases")
public class TestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private TestRunStatus status;

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

    @OneToMany(mappedBy = "testRun", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TestRunTestCase> testRunTestCases = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = TestRunStatus.CREATED;
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }
}

