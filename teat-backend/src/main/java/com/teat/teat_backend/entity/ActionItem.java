package com.teat.teat_backend.entity;

import com.teat.teat_backend.entity.enums.ActionItemStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "action_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"testRunTestCase", "resolutions"})
public class ActionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_run_test_case_id", nullable = false)
    private TestRunTestCase testRunTestCase;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private ActionItemStatus status;

    @Column(name = "assigned_to", length = 100)
    private UUID assignedTo;

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

    @OneToMany(mappedBy = "actionItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ActionItemResolution> resolutions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = ActionItemStatus.OPEN;
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }
}
