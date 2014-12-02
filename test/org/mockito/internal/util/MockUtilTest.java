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

import static org.mockito.Mockito.withSettings;

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
    }

    @Test
    public void should_validate_spy() {
        assertFalse(mockUtil.isSpy("i mock a mock"));
        assertFalse(mockUtil.isSpy(Mockito.mock(List.class)));
        assertFalse(mockUtil.isSpy((Class) null));

        assertTrue(mockUtil.isSpy(Mockito.spy(new ArrayList())));
        assertTrue(mockUtil.isSpy(Mockito.spy(ArrayList.class)));
        assertTrue(mockUtil.isSpy(Mockito.mock(ArrayList.class, withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS))));
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
    public void should_konw_if_type_is_mockable() throws Exception {
        assertFalse(mockUtil.isTypeMockable(FinalClass.class));
        assertFalse(mockUtil.isTypeMockable(int.class));

        assertTrue(mockUtil.isTypeMockable(SomeClass.class));
        assertTrue(mockUtil.isTypeMockable(SomeInterface.class));
    }
}
