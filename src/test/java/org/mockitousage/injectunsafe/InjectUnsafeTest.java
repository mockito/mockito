/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.injectunsafe;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.InjectUnsafe;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.FINAL;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.STATIC;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.STATIC_FINAL;

public class InjectUnsafeTest extends TestBase {

    static class UnderTest {
        final A finalInstanceField = new A();
        static B staticField = new B();
        static final C staticFinalField = new C();
    }

    static class A {
        int getFoo() {
            return 0;
        }
    }

    static class B {
        int getFoo() {
            return 0;
        }
    }

    static class C {
        int getFoo() {
            return 0;
        }
    }

    @Spy
    private A spyFinalInstance;

    @Spy
    private B spyStatic;

    // keep this!
    // Our test checks if injection is skipped when InjectUnsafe.staticFields == STATIC
    @SuppressWarnings("unused")
    @Spy
    private C spyStaticFinal;

    @InjectMocks
    @InjectUnsafe({FINAL, STATIC, STATIC_FINAL})
    private UnderTest underTest;

    @Before
    public void setup() {
        Mockito.when(underTest.finalInstanceField.getFoo()).thenReturn(1);
        Mockito.when(UnderTest.staticField.getFoo()).thenReturn(2);
    }

    @Test
    public void testSpy() {
        assertEquals(1, underTest.finalInstanceField.getFoo());
        assertEquals(2, UnderTest.staticField.getFoo());
    }

    @Test
    public void testSkippedInjectionOnStaticFinal() {
        assertEquals(0, UnderTest.staticFinalField.getFoo());
    }

    // The exception is thrown because the override (staticFields=STATIC) and thus does not include
    // injecting a spy on the static final field.
    // Not injecting a mock means the method call cannot be intercepted => MissingMethodInvocationException
    @Test
    public void testSkippedStaticFinalThrowsWithWhen() {
        Mockito.when(UnderTest.staticFinalField.getFoo()).thenReturn(3);

        assertEquals(3, UnderTest.staticFinalField.getFoo());
    }

}
