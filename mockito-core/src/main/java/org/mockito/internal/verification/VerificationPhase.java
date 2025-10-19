/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

/** Thread-local marker for the verification window. */
public final class VerificationPhase {
    private static final ThreadLocal<Integer> DEPTH = ThreadLocal.withInitial(() -> 0);

    public static void enter() {
        DEPTH.set(DEPTH.get() + 1);
    }

    public static void exit() {
        int d = DEPTH.get();
        if (d <= 1) DEPTH.remove();
        else DEPTH.set(d - 1);
    }

    public static boolean isActive() {
        return DEPTH.get() > 0;
    }

    /** Clear the marker completely (used on global reset). */
    public static void clear() {
        DEPTH.remove();
    }
}
