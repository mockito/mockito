/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;

public class MockNameImplTest extends TestBase {

    @Test
    public void shouldProvideTheNameForClass() throws Exception {
        //when
        String name = new MockNameImpl(null, SomeClass.class).toString();
        //then
        assertEquals("someClass", name);
    }

    @Test
    public void shouldProvideTheNameForAnonymousClass() throws Exception {
        //given
        SomeInterface anonymousInstance = new SomeInterface() {};
        //when
        String name = new MockNameImpl(null, anonymousInstance.getClass()).toString();
        //then
        assertEquals("someInterface", name);
    }

    @Test
    public void shouldProvideTheGivenName() throws Exception {
        //when
        String name = new MockNameImpl("The Hulk", SomeClass.class).toString();
        //then
        assertEquals("The Hulk", name);
    }

    private class SomeClass {}
    private class SomeInterface {}
}