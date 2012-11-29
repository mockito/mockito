/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.NoOp;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MockUtilTest extends TestBase {
    
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void shouldGetHandler() {
        List mock = Mockito.mock(List.class);
        assertNotNull(mockUtil.getMockHandler(mock));
    }

    @Test 
    public void shouldScreamWhenEnhancedButNotAMockPassed() {
        Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        try {
            mockUtil.getMockHandler(o);
            fail();
        } catch (NotAMockException e) {}
    }

    @Test (expected=NotAMockException.class)
    public void shouldScreamWhenNotAMockPassed() {
        mockUtil.getMockHandler("");
    }
    
    @Test (expected=MockitoException.class)
    public void shouldScreamWhenNullPassed() {
        mockUtil.getMockHandler(null);
    }
    
    @Test
    public void shouldValidateMock() {
        assertFalse(mockUtil.isMock("i mock a mock"));
        assertTrue(mockUtil.isMock(Mockito.mock(List.class)));
    }

    @Test
    public void shouldValidateSpy() {
        assertFalse(mockUtil.isSpy("i mock a mock"));
        assertFalse(mockUtil.isSpy(Mockito.mock(List.class)));
        assertTrue(mockUtil.isSpy(Mockito.spy(new ArrayList())));
    }

    @Test
    public void should_redefine_MockName_if_default() {
        List mock = Mockito.mock(List.class);
        mockUtil.maybeRedefineMockName(mock, "newName");

        Assertions.assertThat(mockUtil.getMockName(mock).toString()).isEqualTo("newName");
    }

    @Test
    public void should_not_redefine_MockName_if_default() {
        List mock = Mockito.mock(List.class, "original");
        mockUtil.maybeRedefineMockName(mock, "newName");

        Assertions.assertThat(mockUtil.getMockName(mock).toString()).isEqualTo("original");
    }

    final class FinalClass {}
    class SomeClass {}
    interface SomeInterface {}

    @Test
    public void shouldKnowIfTypeIsMockable() throws Exception {
        assertFalse(mockUtil.isTypeMockable(FinalClass.class));
        assertFalse(mockUtil.isTypeMockable(int.class));

        assertTrue(mockUtil.isTypeMockable(SomeClass.class));
        assertTrue(mockUtil.isTypeMockable(SomeInterface.class));
    }
}
