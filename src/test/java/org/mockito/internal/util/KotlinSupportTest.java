/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.lang.reflect.Method;
import org.junit.Test;
import org.mockito.internal.invocation.RealMethod;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class KotlinSupportTest {

    private static class JavaClass {
        String method() {
            return "";
        }
    }

    private interface JavaInterface {
        String method();
    }

    @Test
    public void hasDefaultImplementation_should_return_false_if_the_given_method_is_not_abstract()
        throws NoSuchMethodException {
        assertFalse(KotlinSupport.hasDefaultImplementation(JavaClass.class.getDeclaredMethod("method")));
    }

    @Test
    public void hasDefaultImplementation_should_return_false_if_the_given_interface_method_is_not_Kotlin_function()
        throws NoSuchMethodException {
        assertFalse(KotlinSupport.hasDefaultImplementation(JavaInterface.class.getMethod("method")));
    }

    @Test
    public void getDefaultImplementation_should_return_notFound_if_the_given_method_is_not_abstract()
        throws NoSuchMethodException {
        JavaInterface mock = mock(JavaInterface.class);
        Method method = JavaClass.class.getDeclaredMethod("method");
        RealMethod notFound = RealMethod.IsIllegal.INSTANCE;
        assertSame(notFound, KotlinSupport.getDefaultImplementation(mock, method, new Object[0], notFound));
    }

    @Test
    public void getDefaultImplementation_should_return_notFound_if_the_given_interface_method_is_not_Kotlin_function()
        throws NoSuchMethodException {
        JavaInterface mock = mock(JavaInterface.class);
        Method method = JavaInterface.class.getMethod("method");
        RealMethod notFound = RealMethod.IsIllegal.INSTANCE;
        assertSame(notFound, KotlinSupport.getDefaultImplementation(mock, method, new Object[0], notFound));
    }

}
