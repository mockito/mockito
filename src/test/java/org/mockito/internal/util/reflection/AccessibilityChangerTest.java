/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Observable;

import static org.mockitoutil.VmArgAssumptions.assumeVmArgPresent;

public class AccessibilityChangerTest {

    @SuppressWarnings("unused")
    private Observable whatever;

    @Test
    public void should_enable_and_safely_disable() throws Exception {
        AccessibilityChanger changer = new AccessibilityChanger();
        changer.enableAccess(field("whatever"));
        changer.safelyDisableAccess(field("whatever"));
    }

    @Test(expected = java.lang.AssertionError.class)
    public void safelyDisableAccess_should_fail_when_enableAccess_not_called() throws Exception {
        assumeVmArgPresent("-ea");
        new AccessibilityChanger().safelyDisableAccess(field("whatever"));
    }


    private Field field(String fieldName) throws NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }

}
