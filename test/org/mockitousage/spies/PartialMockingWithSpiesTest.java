/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.spies;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

@Ignore
@SuppressWarnings("unchecked")
public class PartialMockingWithSpiesTest extends TestBase {

    class Person {
        private final String defaultName = "Default name";

        public String getName() {
            return guessName();
        }

        protected String guessName() {
            return defaultName;
        }
    }

    @Mock
    Person mock;

    @Test
    public void shouldCallRealMethdsEvenDelegatedToOtherSelfMethod() {
        // when
        String name = mock.getName();

        // then
        assertEquals("Default name", name);
    }

    @Test
    public void shouldVerify() {
        // when
        mock.getName();

        // then
        verify(mock).guessName();
    }

    @Test
    public void shouldStub() {
        // given
        when(mock.guessName()).thenReturn("John");
        // when
        String name = mock.getName();
        // then
        assertEquals("John", name);
    }
}