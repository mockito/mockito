/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;

public final class StaticMockTest {

    @Test
    public void testStaticMockSimple() {
        assertEquals("foo", Dummy.foo());
        try (MockedStatic<Dummy> ignored = Mockito.mockStatic(Dummy.class)) {
            assertNull(Dummy.foo());
        }
        assertEquals("foo", Dummy.foo());
    }

    @Test
    public void testStaticMockWithVerification() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
        }
    }

    @Test
    public void testStaticMockWithVerificationFailed() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            assertThatThrownBy(
                            () -> {
                                dummy.verify(Dummy::foo);
                            })
                    .isInstanceOf(WantedButNotInvoked.class)
                    .hasMessageContaining("there were zero interactions with this mock");
        }
    }

    @Test
    public void testStaticMockWithNoInteractions() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            dummy.verifyNoInteractions();
        }
    }

    @Test
    public void testStaticMockWithNoInteractionsFailed() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            assertThatThrownBy(
                            () -> {
                                dummy.verifyNoInteractions();
                            })
                    .isInstanceOf(NoInteractionsWanted.class)
                    .hasMessageContaining("No interactions wanted here")
                    .hasMessageContaining("above is the only interaction with this mock.");
        }
    }

    @Test
    public void testStaticMockWithNoMoreInteractions() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
            dummy.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testStaticMockWithNoMoreInteractionsFailed() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            assertThatThrownBy(
                            () -> {
                                dummy.verifyNoInteractions();
                            })
                    .isInstanceOf(NoInteractionsWanted.class)
                    .hasMessageContaining("No interactions wanted here")
                    .hasMessageContaining("above is the only interaction with this mock.");
        }
    }

    @Test
    public void testStaticMockWithDefaultAnswer() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class, invocation -> "bar")) {
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
        }
    }

    @Test
    public void testStaticMockWithRealMethodCall() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenCallRealMethod();
            assertEquals("foo", Dummy.foo());
            dummy.verify(Dummy::foo);
        }
    }

    @Test
    public void testStaticMockReset() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            dummy.reset();
            assertNull(Dummy.foo());
        }
    }

    @Test
    public void testStaticMockClear() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.clearInvocations();
            dummy.verifyNoInteractions();
        }
    }

    @Test
    public void testStaticMockDoesNotAffectDifferentThread() throws InterruptedException {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
            AtomicReference<String> reference = new AtomicReference<>();
            Thread thread = new Thread(() -> reference.set(Dummy.foo()));
            thread.start();
            thread.join();
            assertEquals("foo", reference.get());
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo, times(2));
        }
    }

    @Test
    public void testStaticMockCanCoexistWithMockInDifferentThread() throws InterruptedException {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
            AtomicReference<String> reference = new AtomicReference<>();
            Thread thread =
                    new Thread(
                            () -> {
                                try (MockedStatic<Dummy> dummy2 = Mockito.mockStatic(Dummy.class)) {
                                    dummy2.when(Dummy::foo).thenReturn("qux");
                                    reference.set(Dummy.foo());
                                }
                            });
            thread.start();
            thread.join();
            assertEquals("qux", reference.get());
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo, times(2));
        }
    }

    @Test
    public void testStaticMockMustBeExclusiveInScopeWithinThread() {
        assertThatThrownBy(
                        () -> {
                            try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class);
                                    MockedStatic<Dummy> duplicate =
                                            Mockito.mockStatic(Dummy.class)) {}
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("static mocking is already registered in the current thread");
    }

    @Test
    public void testStaticMockVoid() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            Dummy.fooVoid("bar");
            assertNull(Dummy.var1);
            dummy.verify(() -> Dummy.fooVoid("bar"));
        }
        Dummy.fooVoid("bar");
        assertEquals("bar", Dummy.var1);
    }

    @Test
    public void testStaticMockMustUseValidMatchers() {
        try (MockedStatic<Dummy> mockedClass = Mockito.mockStatic(Dummy.class)) {
            assertThatThrownBy(
                            () -> {
                                mockedClass
                                        .when(() -> Dummy.fooVoid("foo", any()))
                                        .thenReturn(null);
                            })
                    .hasMessageContaining("Invalid use of argument matchers!");

            Dummy.fooVoid("foo", "bar");
        }
    }

    @Test
    public void testStaticMockVarargs() {
        assertEquals("foobar", Dummy.fooVarargs("foo", "bar"));
        try (MockedStatic<Dummy> ignored = Mockito.mockStatic(Dummy.class)) {
            assertNull(Dummy.fooVarargs("foo", "bar"));
        }
        assertEquals("foobar", Dummy.fooVarargs("foo", "bar"));
    }

    static class Dummy {

        static String var1 = null;

        static String foo() {
            return "foo";
        }

        static void fooVoid(String var2) {
            var1 = var2;
        }

        static void fooVoid(String var2, String var3) {
            var1 = var2;
        }

        static String fooVarargs(String... args) {
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(arg);
            }
            return sb.toString();
        }
    }
}
