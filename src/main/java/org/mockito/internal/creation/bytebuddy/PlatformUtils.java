/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

/**
 * Helpers for various platform detection mechanisms related to the ByteBuddy mock maker.
 *
 * @author Ashley Scopes
 */
final class PlatformUtils {
    private PlatformUtils() {
        // Static-only class.
    }

    static boolean isAndroidPlatform() {
        return System.getProperty("java.specification.vendor", "")
                .toLowerCase()
                .contains("android");
    }

    static boolean isProbablyTermuxEnvironment() {
        boolean isLinux = System.getProperty("os.name", "").equalsIgnoreCase("linux");
        boolean isInTermuxData =
                System.getProperty("java.home", "").toLowerCase().contains("/com.termux/");
        return isLinux && isInTermuxData;
    }
}
