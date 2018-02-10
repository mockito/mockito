/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;
import org.mockitoutil.TestBase;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

public class InvocationFactoryTest extends TestBase {
    static class TestClass {
        public String testMethod() throws Throwable {
            return "un-mocked";
        }
    }

    final TestClass mock = spy(TestClass.class);

    @Test
    public void call_method_that_throws_a_throwable() throws Throwable {
        Invocation invocation = Mockito.framework().getInvocationFactory().createInvocation(mock,
            withSettings().build(TestClass.class),
            TestClass.class.getDeclaredMethod("testMethod"),
            new InvocationFactory.RealMethodBehavior() {
            @Override
            public Object call() throws Throwable {
                throw new Throwable("mocked");
            }
        });

        try {
            Mockito.mockingDetails(mock).getMockHandler().handle(invocation);
        } catch (Throwable t) {
            assertEquals("mocked", t.getMessage());
            return;
        }

        fail();
    }

    @Test
    public void call_method_that_returns_a_string() throws Throwable {
        Invocation invocation = Mockito.framework().getInvocationFactory().createInvocation(mock,
            withSettings().build(TestClass.class),
            TestClass.class.getDeclaredMethod("testMethod"),
            new InvocationFactory.RealMethodBehavior() {
                @Override
                public Object call() throws Throwable {
                    return "mocked";
                }
            });

        Object ret = Mockito.mockingDetails(mock).getMockHandler().handle(invocation);
        assertEquals("mocked", ret);
    }

    @Test
    public void deprecated_api_still_works() throws Throwable {
        Invocation invocation = Mockito.framework().getInvocationFactory().createInvocation(mock,
            withSettings().build(TestClass.class),
            TestClass.class.getDeclaredMethod("testMethod"),
            new Callable() {
                public Object call() throws Exception {
                    return "mocked";
                }
            });

        Object ret = Mockito.mockingDetails(mock).getMockHandler().handle(invocation);
        assertEquals("mocked", ret);
    }
}
