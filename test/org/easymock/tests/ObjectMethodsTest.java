/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.easymock.MockControl;
import org.easymock.internal.MockInvocationHandler;
import org.easymock.internal.ObjectMethodsFilter;
import org.junit.*;

public class ObjectMethodsTest {
    private MockControl<EmptyInterface> control;

    private EmptyInterface mock;

    private interface EmptyInterface {
    }

    @Before
    public void setup() {
        control = MockControl.createControl(EmptyInterface.class);
        mock = control.getMock();
    }

    @Test
    public void equalsBeforeActivation() {
        assertEquals(mock, mock);
        assertTrue(!mock.equals(null));
    }

    @Test
    public void equalsAfterActivation() {
        control.replay();
        assertEquals(mock, mock);
        assertTrue(!mock.equals(null));
    }

    @Test
    public void testHashCode() {
        int hashCodeBeforeActivation = mock.hashCode();
        control.replay();
        int hashCodeAfterActivation = mock.hashCode();
        assertEquals(hashCodeBeforeActivation, hashCodeAfterActivation);
    }

    @Test
    public void toStringBeforeActivation() {
        assertEquals("EasyMock for " + EmptyInterface.class.toString(), mock
                .toString());
    }

    @Test
    public void toStringAfterActivation() {
        control.replay();
        assertEquals("EasyMock for " + EmptyInterface.class.toString(), mock
                .toString());
    }

    private static class MockedClass {
    }

    private static class DummyProxy extends MockedClass {
    }

    // if the class is no Proxy, ObjectMethodFilter should use the
    // superclasses' name. This is needed for the class extension.
    @Test
    public void toStringForClasses() throws Throwable {
        ObjectMethodsFilter filter = new ObjectMethodsFilter(Object.class, null, null);
        Method toString = Object.class.getMethod("toString", new Class[0]);
        assertEquals("EasyMock for " + MockedClass.class.toString(), filter
                .invoke(new DummyProxy(), toString, new Object[0]));
    }
    
    @Ignore
    @Test
    public void whatHappensWhenClassToMockIsAnonymous() throws Exception {
        fail();
    }

}
