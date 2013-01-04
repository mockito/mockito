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
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MockUtilTest extends TestBase {
    
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void should_get_handler() {
        List mock = Mockito.mock(List.class);
        assertNotNull(mockUtil.getMockHandler(mock));
    }

    @Test 
    public void should_scream_when_enhanced_but_not_a_mock_passed() {
        Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        try {
            mockUtil.getMockHandler(o);
            fail();
        } catch (NotAMockException e) {}
    }

    @Test (expected=NotAMockException.class)
    public void should_scream_when_not_a_mock_passed() {
        mockUtil.getMockHandler("");
    }
    
    @Test (expected=MockitoException.class)
    public void should_scream_when_null_passed() {
        mockUtil.getMockHandler(null);
    }

    @Test
    public void should_get_mock_settings() {
        List mock = Mockito.mock(List.class);
        assertNotNull(mockUtil.getMockSettings(mock));
    }

    @Test
    public void should_validate_mock() {
        assertFalse(mockUtil.isMock("i mock a mock"));
        assertTrue(mockUtil.isMock(Mockito.mock(List.class)));
        assertFalse(mockUtil.isMock((Class) null));
        assertFalse(mockUtil.isMock(String.class));
        assertTrue(mockUtil.isMock(Mockito.mock(List.class).getClass()));
    }

    @Test
    public void should_validate_spy() {
        assertFalse(mockUtil.isSpy("i mock a mock"));
        assertFalse(mockUtil.isSpy(Mockito.mock(List.class)));
        assertTrue(mockUtil.isSpy(Mockito.spy(new ArrayList())));
        assertFalse(mockUtil.isSpy((Class) null));
        assertFalse(mockUtil.isSpy(String.class));
        assertFalse(mockUtil.isSpy(Mockito.mock(List.class).getClass()));
        assertTrue(mockUtil.isSpy(Mockito.spy(new ArrayList()).getClass()));
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
