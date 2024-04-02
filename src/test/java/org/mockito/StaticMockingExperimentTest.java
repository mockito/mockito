package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;
import org.mockito.invocation.MockHandler;
import org.mockitoutil.TestBase;

public class StaticMockingExperimentTest extends TestBase {

    Foo mock = Mockito.mock(Foo.class);
    MockHandler handler = Mockito.mockingDetails(mock).getMockHandler();
    Method staticMethod;
    InvocationFactory.RealMethodBehavior realMethod =
        new InvocationFactory.RealMethodBehavior() {
            @Override
            public Object call() throws Throwable {
                return null;
            }
        };

    @Before
    public void before() throws Throwable {
        staticMethod = Foo.class.getDeclaredMethod("staticMethod", String.class);
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void verify_static_method() throws Throwable {
        // register staticMethod call on mock
        Invocation invocation =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "some arg");
        handler.handle(invocation);

        // verify staticMethod on mock
        // Mockito cannot capture static methods so we will simulate this scenario in 3 steps:
        // 1. Call standard 'verify' method. Internally, it will add verificationMode to the thread
        // local state.
        //  Effectively, we indicate to Mockito that right now we are about to verify a method call
        // on this mock.
        verify(mock);
        // 2. Create the invocation instance using the new public API
        //  Mockito cannot capture static methods but we can create an invocation instance of that
        // static invocation
        Invocation verification =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "some arg");
        // 3. Make Mockito handle the static method invocation
        //  Mockito will find verification mode in thread local state and will try verify the
        // invocation
        handler.handle(verification);

        // verify zero times, method with different argument
        verify(mock, times(0));
        Invocation differentArg =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "different arg");
        handler.handle(differentArg);
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void verification_failure_static_method() throws Throwable {
        // register staticMethod call on mock
        Invocation invocation =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "foo");
        handler.handle(invocation);

        // verify staticMethod on mock
        verify(mock);
        Invocation differentArg =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "different arg");

        try {
            handler.handle(differentArg);
            fail();
        } catch (ArgumentsAreDifferent e) {
        }
    }

    @Test
    public void stubbing_static_method() throws Throwable {
        // register staticMethod call on mock
        Invocation invocation =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "foo");
        handler.handle(invocation);

        // register stubbing
        when(null).thenReturn("hey");

        // validate stubbed return value
        assertEquals("hey", handler.handle(invocation));
        assertEquals("hey", handler.handle(invocation));

        // default null value is returned if invoked with different argument
        Invocation differentArg =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "different arg");
        assertEquals(null, handler.handle(differentArg));
    }

    @Test
    public void do_answer_stubbing_static_method() throws Throwable {
        // register stubbed return value
        Object ignored = doReturn("hey").when(mock);

        // complete stubbing by triggering an invocation that needs to be stubbed
        Invocation invocation =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "foo");
        handler.handle(invocation);

        // validate stubbed return value
        assertEquals("hey", handler.handle(invocation));
        assertEquals("hey", handler.handle(invocation));

        // default null value is returned if invoked with different argument
        Invocation differentArg =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "different arg");
        assertEquals(null, handler.handle(differentArg));
    }

    @Test
    public void verify_no_more_interactions() throws Throwable {
        // works for now because there are not interactions
        verifyNoMoreInteractions(mock);

        // register staticMethod call on mock
        Invocation invocation =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                staticMethod,
                                realMethod,
                                "foo");
        handler.handle(invocation);

        // fails now because we have one static invocation recorded
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
        }
    }

    @Test
    public void stubbing_new() throws Throwable {
        Constructor<Foo> ctr = Foo.class.getConstructor(String.class);
        Method adapter = ConstructorMethodAdapter.class.getDeclaredMethods()[0];

        // stub constructor
        Object ignored = doReturn(new Foo("hey!")).when(mock);
        Invocation constructor =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                adapter,
                                realMethod,
                                ctr,
                                "foo");
        handler.handle(constructor);

        // test stubbing
        Object result = handler.handle(constructor);
        assertEquals("foo:hey!", result.toString());

        // stubbing miss
        Invocation differentArg =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                adapter,
                                realMethod,
                                ctr,
                                "different arg");
        Object result2 = handler.handle(differentArg);
        assertEquals(null, result2);
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void verifying_new() throws Throwable {
        Constructor<Foo> ctr = Foo.class.getConstructor(String.class);
        Method adapter = ConstructorMethodAdapter.class.getDeclaredMethods()[0];

        // invoke constructor
        Invocation constructor =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                adapter,
                                realMethod,
                                ctr,
                                "matching arg");
        handler.handle(constructor);

        // verify successfully
        verify(mock);
        handler.handle(constructor);

        // verification failure
        verify(mock);
        Invocation differentArg =
                Mockito.framework()
                        .getInvocationFactory()
                        .createInvocation(
                                mock,
                                withSettings().build(Foo.class),
                                adapter,
                                realMethod,
                                ctr,
                                "different arg");
        try {
            handler.handle(differentArg);
            fail();
        } catch (WantedButNotInvoked e) {
            assertThat(e.getMessage()).contains("matching arg").contains("different arg");
        }
    }

    /**
     * Adapts constructor to method calls needed to work with Mockito API.
     */
    interface ConstructorMethodAdapter {
        Object construct(Constructor constructor, Object... args);
    }
}
