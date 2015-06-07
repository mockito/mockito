/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.util.Observable;

import org.junit.Ignore;
import org.junit.Test;

public class AccessibilityChangerTest {

    @SuppressWarnings("unused")
    private Observable whatever;

    @Test
    public void should_enable_and_safely_disable() throws Exception {
        final AccessibilityChanger changer = new AccessibilityChanger();
        changer.enableAccess(field("whatever"));
        changer.safelyDisableAccess(field("whatever"));
    }

    @Test(expected = java.lang.AssertionError.class)
    @Ignore("should be run only when -ea is passed to the VM")
    public void safelyDisableAccess_should_fail_when_enableAccess_not_called() throws Exception {
        new AccessibilityChanger().safelyDisableAccess(field("whatever"));
    }

    private Field field(final String fieldName) throws NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }
}
