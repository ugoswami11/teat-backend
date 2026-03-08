package com.teat.teat_backend.util;

import java.util.UUID;

/**
 * Utility provider for authentication-related operations.
 * This will be enhanced in Phase 2 when JWT authentication is implemented.
 */
public class AuthProvider {

    /**
     * Get the current authenticated user's UUID.
     * For Phase 1, returns a default UUID.
     * Will be replaced with actual JWT claim extraction in Phase 2.
     */
    public static UUID getCurrentUserId() {
        // TODO: Extract from SecurityContext in Phase 2
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    /**
     * Get the current authenticated user's username.
     * For Phase 1, returns a default value.
     * Will be replaced with actual JWT claim extraction in Phase 2.
     */
    public static String getCurrentUsername() {
        // TODO: Extract from SecurityContext in Phase 2
        return "system-user";
    }
}
