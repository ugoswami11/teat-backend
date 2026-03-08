package com.teat.teat_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "action_item_resolutions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "actionItem")
public class ActionItemResolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "action_item_id", nullable = false)
    private ActionItem actionItem;

    @Column(name = "resolution_details", nullable = false, columnDefinition = "text")
    private String resolutionDetails;

    @Column(name = "evidence_link", length = 500)
    private String evidenceLink;

    @Column(name = "resolved_by", nullable = false, length = 100)
    private UUID resolvedBy;

    @Column(name = "resolved_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime resolvedAt;

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
        if (resolvedAt == null) {
            resolvedAt = OffsetDateTime.now();
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }
}
