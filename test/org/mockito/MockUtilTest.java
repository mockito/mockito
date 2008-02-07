/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.*;
import static org.mockito.internal.MockUtil.*;

import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest extends TestBase {

    @SuppressWarnings("unchecked")
    @Test 
    public void shouldGetHandler() {
        List mock = Mockito.mock(List.class);
        assertNotNull(getMockHandler(mock));
    }

    @Test 
    public void shouldScreamWhenEnhancedButNotAMockPassed() {
        Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        try {
            getMockHandler(o);
            fail();
        } catch (NotAMockException e) {}
    }

    @Test (expected=NotAMockException.class)
    public void shouldScreamWhenNotAMockPassed() {
        getMockHandler("");
    }
    
    @Test (expected=MockitoException.class)
    public void shouldScreamWhenNullPassed() {
        getMockHandler(null);
    }
    
    @Test
    public void shouldValidateMock() {
        assertFalse(isMock("i mock a mock"));
        assertTrue(isMock(Mockito.mock(List.class)));
    }
}
