package org.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockitoutil.TestBase;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * This test is an experimental use of Mockito API to simulate static mocking.
 * Other frameworks (like Powermock) can use it to build decent support for static mocking.
 */
public class StaticMockingExperimentTest extends TestBase {

    Foo mock = Mockito.mock(Foo.class);
    MockHandler handler = Mockito.mockingDetails(mock).getMockHandler();
    Method staticMethod;

    @Before public void before() throws Throwable {
        staticMethod = Foo.class.getDeclaredMethod("staticMethod", String.class);
    }

    @Test
    public void verify_static_method() throws Throwable {
        //register staticMethod call on mock
        Invocation invocation = Mockito.framework().createInvocation(mock, withSettings().build(Foo.class), staticMethod,
            "some arg");
        handler.handle(invocation);

        //verify staticMethod on mock
        Mockito.verify(mock);
        Invocation verification = Mockito.framework().createInvocation(mock, withSettings().build(Foo.class), staticMethod,
            "some arg");

        handler.handle(verification);

        //verify zero times, method with different argument
        Mockito.verify(mock, times(0));
        Invocation differentArg = Mockito.framework().createInvocation(mock, withSettings().build(Foo.class), staticMethod,
            "different arg");
        handler.handle(differentArg);
    }

    @Test
    public void verification_failure_static_method() throws Throwable {
        //register staticMethod call on mock
        Invocation invocation = Mockito.framework().createInvocation(mock, withSettings().build(Foo.class), staticMethod,
            "foo");
        handler.handle(invocation);

        //verify staticMethod on mock
        Mockito.verify(mock);
        Invocation differentArg = Mockito.framework().createInvocation(mock, withSettings().build(Foo.class), staticMethod,
            "different arg");

        try {
            handler.handle(differentArg);
            fail();
        } catch (ArgumentsAreDifferent e) {}
    }

    @Test
    public void stubbing_static_method() throws Throwable {
        //register staticMethod call on mock
        Invocation invocation = Mockito.framework().createInvocation(mock, withSettings().build(Foo.class), staticMethod,
            "foo");
        handler.handle(invocation);

        //register stubbing
        when(null).thenReturn("hey");

        //validate stubbed return value
        assertEquals("hey", handler.handle(invocation));
        assertEquals("hey", handler.handle(invocation));

        //default null value is returned if invoked with different argument
        Invocation differentArg = Mockito.framework().createInvocation(mock, withSettings().build(Foo.class), staticMethod,
            "different arg");
        assertEquals(null, handler.handle(differentArg));
    }

    @Test
    public void do_answer_stubbing_static_method() throws Throwable {
        //register stubbed return value
        doReturn("hey").when(mock);

        //complete stubbing by triggering an invocation that needs to be stubbed
        Invocation invocation = Mockito.framework()
            .createInvocation(mock, withSettings().build(Foo.class), staticMethod, "foo");
        handler.handle(invocation);

        //validate stubbed return value
        assertEquals("hey", handler.handle(invocation));
        assertEquals("hey", handler.handle(invocation));

        //default null value is returned if invoked with different argument
        Invocation differentArg = Mockito.framework().createInvocation(mock, withSettings().build(Foo.class), staticMethod,
            "different arg");
        assertEquals(null, handler.handle(differentArg));
    }

    static class Foo {
        public static String staticMethod(String arg) {
            return "";
        }
    }
}
