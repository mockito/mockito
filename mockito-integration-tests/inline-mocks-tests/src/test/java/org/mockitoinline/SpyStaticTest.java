/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.withSettings;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public final class SpyStaticTest {

    @Test
    public void testStaticSpyCallsRealMethodByDefault() {
        assertEquals("foo", Dummy.foo());
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            assertEquals("foo", Dummy.foo());
        }
        assertEquals("foo", Dummy.foo());
    }

    @Test
    public void testStaticSpyWithStubbing() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            assertEquals("foobar", Dummy.fooWithArg("bar"));
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            assertEquals("foobar2", Dummy.fooWithArg("bar2"));
        }
    }

    @Test
    public void testStaticSpyWithVerification() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            assertEquals("foo", Dummy.foo());
            spy.verify(Dummy::foo);
        }
    }

    @Test
    public void testStaticSpyVoidMethod() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            Dummy.var1 = null;
            Dummy.fooVoid("bar");
            assertEquals("bar", Dummy.var1);
            spy.verify(() -> Dummy.fooVoid("bar"));
            Dummy.fooVoid("hello");
            assertEquals("hello", Dummy.var1);
            spy.verify(() -> Dummy.fooVoid("hello"));
        }
    }

    @Test
    public void testStaticSpyResetRestoresRealMethods() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            spy.reset();
            assertEquals("foo", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyScopeRestoresOriginalBehavior() {
        assertEquals("foo", Dummy.foo());
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
        }
        assertEquals("foo", Dummy.foo());
    }

    @Test
    public void testStaticSpyDoesNotAffectDifferentThread() throws InterruptedException {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());

            AtomicReference<String> result = new AtomicReference<>();
            Thread thread = new Thread(() -> result.set(Dummy.foo()));
            thread.start();
            thread.join();

            assertEquals("foo", result.get());
        }
    }

    @Test
    public void testStaticSpyNamedOverload() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class, "mySpy")) {
            assertEquals("foo", Dummy.foo());
            spy.when(Dummy::foo).thenReturn("named");
            assertEquals("named", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyWithMockSettings() {
        try (MockedStatic<Dummy> spy =
                Mockito.spyStatic(Dummy.class, withSettings().name("settingsSpy"))) {
            assertEquals("foo", Dummy.foo());
            spy.when(Dummy::foo).thenReturn("settings");
            assertEquals("settings", Dummy.foo());
        }
    }

    static class Dummy {

        static String var1 = null;

        static String foo() {
            return "foo";
        }

        static String fooWithArg(String arg) {
            return "foo" + arg;
        }

        static void fooVoid(String var2) {
            var1 = var2;
        }
    }
}
