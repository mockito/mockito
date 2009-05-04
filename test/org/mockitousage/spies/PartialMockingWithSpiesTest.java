/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.spies;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class PartialMockingWithSpiesTest extends TestBase {

    @After
    public void validateMockitoUsage() {
        Mockito.validateMockitoUsage();
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