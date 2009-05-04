package org.mockito.internal.util.copy;

import java.lang.reflect.Field;

public class AccessibilityChanger {
    
    private Boolean wasAccessible = null;

    /**
     * safely disables access
     */
    public void safelyDisableAccess(Field field) {
        assert wasAccessible != null;
        try {
            field.setAccessible(wasAccessible);
        } catch (Throwable t) {
            //ignore
        }
    }

    /**
     * changes the field accessibility and returns true if accessibility was changed
     */
    public void enableAccess(Field field) {
        wasAccessible = field.isAccessible();
        field.setAccessible(true);
    }
}