/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class RestrictedObjectMethodsTest extends TestBase {

    @Mock IMethods mock;

    @Ignore
    @Test
    public void shouldNotAllowStubbingRestrictedMethods() {
        try {
            when(mock.hashCode()).thenReturn(1);
            fail();
        } catch(MockitoException e) {
            assertEquals("Cannot stub hashCode() method", e.getMessage());
        }
        
        try {
            when(mock.equals(null)).thenReturn(false);
            fail();
        } catch(MockitoException e) {
            assertEquals("Cannot stub equals() method", e.getMessage());
        }
    }
    
    @Ignore
    @Test
    public void shouldNotAllowVerifyingRestrictedMethods() {
        //TODO: after 1.7 exception message should mention those methods are not verifiable
        verify(mock).toString();
        verify(mock).hashCode();
        verify(mock).equals(null);
    }
}