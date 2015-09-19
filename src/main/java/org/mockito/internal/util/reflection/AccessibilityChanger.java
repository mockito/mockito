/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.AccessibleObject;

public class AccessibilityChanger {
    
    private Boolean wasAccessible = null;

    /**
     * safely disables access
     */
    public void safelyDisableAccess(AccessibleObject accessibleObject) {
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
    public void enableAccess(AccessibleObject accessibleObject) {
        wasAccessible = accessibleObject.isAccessible();
        accessibleObject.setAccessible(true);
    }
}
