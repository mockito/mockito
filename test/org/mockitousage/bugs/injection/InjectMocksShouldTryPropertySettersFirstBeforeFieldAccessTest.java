/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.injection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Issue 211 : @InjectMocks should carry out their work by the method (and not by field) if available 
 */
@RunWith(MockitoJUnitRunner.class)
public class InjectMocksShouldTryPropertySettersFirstBeforeFieldAccessTest {
    @Mock List<?> fieldAccess;
    @Mock List<?> propertySetterAccess;
    @InjectMocks BeanAwaitingInjection awaitingInjection;

    @Test
    public void shouldInjectUsingPropertySetterIfAvailable() {
        assertTrue(awaitingInjection.propertySetterUsed);
    }

    @Test
    public void shouldInjectFieldIfNoSetter() {
        assertEquals(fieldAccess, awaitingInjection.fieldAccess);
    }

    static class BeanAwaitingInjection {
        List<?> fieldAccess;
        List<?> propertySetterAccess;
        boolean propertySetterUsed;

        public void setPropertySetterAccess(List<?> propertySetterAccess) {
            // don't care if field is set, the setter can do whatever it want.
            propertySetterUsed = true;
        }
    }

}
