/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

public class AndroidPlatform {

    private static int getSdkInt() {
        try {
            return Class.forName("android.os.Build$VERSION").getField("SDK_INT").getInt(null);
        } catch (ReflectiveOperationException e) {
            return 0;
        }
    }

    private static int getExtensionVersion(int sdk) {
        try {
            return (int)
                    Class.forName("android.os.ext.SdkExtensions")
                            .getMethod("getExtensionVersion", int.class)
                            .invoke(null, sdk);
        } catch (ReflectiveOperationException e) {
            return 0;
        }
    }

    public static boolean isStackWalkerUsable() {
        // StackWalker on Android had a bug that is fixed in Android Baklava (36)
        // or SDK extension train M2025-05 (17) and later. See https://r.android.com/3548340.
        return getSdkInt() >= 36 || getSdkInt() >= 31 && getExtensionVersion(31) >= 17;
    }
}
