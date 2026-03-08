package com.teat.teat_backend.exception;

import java.util.UUID;

/**
 * Exception thrown when an optimistic locking failure occurs.
 * This happens when a resource is updated with a stale version number.
 */
public class OptimisticLockException extends RuntimeException {

    private final String resourceName;
    private final UUID resourceId;

    public OptimisticLockException(String resourceName, UUID resourceId) {
        super(String.format("Failed to update %s (ID: %s). The resource has been modified by another user. Please refresh and try again.",
            resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public OptimisticLockException(String message) {
        super(message);
        this.resourceName = null;
        this.resourceId = null;
    }

    public String getResourceName() {
        return resourceName;
    }

    public UUID getResourceId() {
        return resourceId;
    }
}
