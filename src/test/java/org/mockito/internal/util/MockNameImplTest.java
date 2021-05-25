/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class MockNameImplTest extends TestBase {

    @Test
    public void shouldProvideTheNameForClass() throws Exception {
        // when
        String name = new MockNameImpl(null, SomeClass.class, false).toString();
        // then
        assertEquals("someClass", name);
    }

    @Test
    public void shouldProvideTheNameForClassOnStaticMock() throws Exception {
        // when
        String name = new MockNameImpl(null, SomeClass.class, true).toString();
        // then
        assertEquals("SomeClass.class", name);
    }

    @Test
    public void shouldProvideTheNameForAnonymousClass() throws Exception {
        // given
        SomeInterface anonymousInstance = new SomeInterface() {};
        // when
        String name = new MockNameImpl(null, anonymousInstance.getClass(), false).toString();
        // then
        assertEquals("someInterface", name);
    }

    @Test
    public void shouldProvideTheNameForAnonymousClassOnStatic() throws Exception {
        // given
        SomeInterface anonymousInstance = new SomeInterface() {};
        // when
        String name = new MockNameImpl(null, anonymousInstance.getClass(), true).toString();
        // then
        assertEquals("SomeInterface$.class", name);
    }

    @Test
    public void shouldProvideTheGivenName() throws Exception {
        // when
        String name = new MockNameImpl("The Hulk", SomeClass.class, false).toString();
        // then
        assertEquals("The Hulk", name);
    }

    @Test
    public void shouldProvideTheGivenNameOnStatic() throws Exception {
        // when
        String name = new MockNameImpl("The Hulk", SomeClass.class, true).toString();
        // then
        assertEquals("The Hulk", name);
    }

    private class SomeClass {}

    private class SomeInterface {}
}
