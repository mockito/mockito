/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.withSettings;

@SuppressWarnings("unchecked")
public class MockUtilTest extends TestBase {

    @Test
    public void should_get_handler() {
        List<?> mock = Mockito.mock(List.class);
        assertNotNull(MockUtil.getMockHandler(mock));
    }

    @Test (expected=NotAMockException.class)
    public void should_scream_when_not_a_mock_passed() {
        MockUtil.getMockHandler("");
    }

    @Test (expected=MockitoException.class)
    public void should_scream_when_null_passed() {
        MockUtil.getMockHandler(null);
    }

    @Test
    public void should_get_mock_settings() {
        List<?> mock = Mockito.mock(List.class);
        assertNotNull(MockUtil.getMockSettings(mock));
    }

    @Test
    public void should_validate_mock() {
        assertFalse(MockUtil.isMock("i mock a mock"));
        assertTrue(MockUtil.isMock(Mockito.mock(List.class)));
    }

    @Test
    public void should_validate_spy() {
        assertFalse(MockUtil.isSpy("i mock a mock"));
        assertFalse(MockUtil.isSpy(Mockito.mock(List.class)));
        assertFalse(MockUtil.isSpy(null));

        assertTrue(MockUtil.isSpy(Mockito.spy(new ArrayList())));
        assertTrue(MockUtil.isSpy(Mockito.spy(ArrayList.class)));
        assertTrue(MockUtil.isSpy(Mockito.mock(ArrayList.class, withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS))));
    }

    @Test
    public void should_redefine_MockName_if_default() {
        List<?> mock = Mockito.mock(List.class);
        MockUtil.maybeRedefineMockName(mock, "newName");

        Assertions.assertThat(MockUtil.getMockName(mock).toString()).isEqualTo("newName");
    }

    @Test
    public void should_not_redefine_MockName_if_default() {
        List<?> mock = Mockito.mock(List.class, "original");
        MockUtil.maybeRedefineMockName(mock, "newName");

        Assertions.assertThat(MockUtil.getMockName(mock).toString()).isEqualTo("original");
    }

    final class FinalClass {}
    class SomeClass {}
    interface SomeInterface {}

    @Test
    public void should_know_if_type_is_mockable() throws Exception {
        assertFalse(MockUtil.typeMockabilityOf(FinalClass.class).mockable());
        assertFalse(MockUtil.typeMockabilityOf(int.class).mockable());

        assertTrue(MockUtil.typeMockabilityOf(SomeClass.class).mockable());
        assertTrue(MockUtil.typeMockabilityOf(SomeInterface.class).mockable());
    }
}
