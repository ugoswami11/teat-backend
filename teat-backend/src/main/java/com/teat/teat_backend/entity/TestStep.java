package com.teat.teat_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_step")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "testCase")
public class TestStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "step_description", nullable = false, columnDefinition = "text")
    private String stepDescription;

    @Column(name = "expected_result", columnDefinition = "text")
    private String expectedResult;

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

    @PrePersist
    protected void onCreate() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }
}
