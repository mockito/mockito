/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.spies;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockitoutil.Conditions.methodsInStackTrace;

@SuppressWarnings("unchecked")
public class PartialMockingWithSpiesTest extends TestBase {

    @Before
    public void pleaseMakeStackTracesClean() {
        makeStackTracesClean();
    }

    class InheritMe {
        private String inherited = "100$";
        protected String getInherited() {
            return inherited;
        }
    }

    class Person extends InheritMe {
        private final Name defaultName = new Name("Default name");

        public String getName() {
            return guessName().name;
        }

        Name guessName() {
            return defaultName;
        }

        public String howMuchDidYouInherit() {
            return getInherited();
        }

        public String getNameButDelegateToMethodThatThrows() {
            throwSomeException();
            return guessName().name;
        }

        private void throwSomeException() {
            throw new RuntimeException("boo");
        }
    }

    class Name {
        private final String name;

        public Name(String name) {
            this.name = name;
        }
    }

    Person spy = spy(new Person());

    @Test
    public void shouldCallRealMethdsEvenDelegatedToOtherSelfMethod() {
        // when
        String name = spy.getName();

        // then
        assertEquals("Default name", name);
    }

    @Test
    public void shouldAllowStubbingOfMethodsThatDelegateToOtherMethods() {
        // when
        when(spy.getName()).thenReturn("foo");

        // then
        assertEquals("foo", spy.getName());
    }

    @Test
    public void shouldAllowStubbingWithThrowablesMethodsThatDelegateToOtherMethods() {
        // when
        doThrow(new RuntimeException("appetite for destruction"))
            .when(spy).getNameButDelegateToMethodThatThrows();

        // then
        try {
            spy.getNameButDelegateToMethodThatThrows();
            fail();
        } catch(Exception e) {
            assertEquals("appetite for destruction", e.getMessage());
        }
    }

    @Test
    public void shouldStackTraceGetFilteredOnUserExceptions() {
        try {
            // when
            spy.getNameButDelegateToMethodThatThrows();
            fail();
        } catch (Throwable t) {
            // then
            Assertions.assertThat(t).has(methodsInStackTrace(
                    "throwSomeException",
                    "getNameButDelegateToMethodThatThrows",
                    "shouldStackTraceGetFilteredOnUserExceptions"
                    ));
        }
    }

//    @Test //manual verification
    public void verifyTheStackTrace() {
        spy.getNameButDelegateToMethodThatThrows();
    }

    @Test
    public void shouldVerify() {
        // when
        spy.getName();

        // then
        verify(spy).guessName();
    }

    @Test
    public void shouldStub() {
        // given
        when(spy.guessName()).thenReturn(new Name("John"));
        // when
        String name = spy.getName();
        // then
        assertEquals("John", name);
    }

    @Test
    public void shouldDealWithPrivateFieldsOfSubclasses() {
        assertEquals("100$", spy.howMuchDidYouInherit());
    }
}
