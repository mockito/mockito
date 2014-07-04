/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

public class ThrowsSmartNullsTest extends TestBase {
    private Answer<Object> answer = new ThrowsSmartNulls();

    interface Foo {
        Foo get();

        Foo withArgs(String oneArg, String otherArg);
    }

    @Test
    public void should_return_an_object_that_fails_on_any_method_invocation_for_non_primitives() throws Throwable {
        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "get"));

        try {
            smartNull.get();
            fail();
        } catch (SmartNullPointerException expected) {
        }
    }

    @Test
    public void should_throw_an_exception_on_any_method_invocation_returning_primitives() throws Throwable {
        Method[] declaredMethods = HasPrimitiveMethods.class.getDeclaredMethods();
        assertNotEquals(0, declaredMethods.length);
        for (Method method : declaredMethods) {
            try {
                answer.answer(invocationOf(HasPrimitiveMethods.class, method.getName()));
                fail();
            } catch (SmartNullPointerException expected) {
            }
        }
    }

    @Test
    public void should_return_an_object_that_allows_object_methods() throws Throwable {
        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "get"));

        assertContains("SmartNull returned by", smartNull + "");
        assertContains("foo.get()", smartNull + "");
    }

    @Test
    public void should_print_the_parameters_when_calling_a_method_with_args() throws Throwable {
        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "withArgs", "oompa", "lumpa"));

        assertContains("foo.withArgs", smartNull + "");
        assertContains("oompa", smartNull + "");
        assertContains("lumpa", smartNull + "");
    }

    @Test
    public void should_print_the_parameters_on_SmartNullPointerException_message() throws Throwable {
        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "withArgs", "oompa", "lumpa"));

        try {
            smartNull.get();
            fail();
        } catch (SmartNullPointerException e) {
            assertContains("oompa", e.getMessage());
            assertContains("lumpa", e.getMessage());
        }
    }
}
