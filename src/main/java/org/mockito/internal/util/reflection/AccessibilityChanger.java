/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.AccessibleObject;

public final class AccessibilityChanger {
    public static Boolean wasAccessible = null;

    private AccessibilityChanger() {}

    /**
     * safely disables access
     */
    public static void safelyDisableAccess(AccessibleObject accessibleObject) {
        assert wasAccessible != null : "accessibility info shall not be null";
        try {
            accessibleObject.setAccessible(wasAccessible);
        } catch (Throwable t) {
            //ignore
        }
    }

    /**
     * changes the accessibleObject accessibility and returns true if accessibility was changed
     */
    public static void enableAccess(AccessibleObject accessibleObject) {
        wasAccessible = accessibleObject.isAccessible();
        accessibleObject.setAccessible(true);
    }
}
