/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class ArgumentCaptorTest extends TestBase {

    public class Capture<T> extends ArgumentMatcher<T> {
        private Object argument;

        public boolean matches(Object argument) {
            this.argument = argument;
            return true;
        }

        public T getValue() {
            //TODO: after 1.7 nice instanceof check here?
            //TODO: capture according to what one guy wrote to the mailing list?
            return (T) argument;
        }
    }

    class Person {

        private final int age;

        public Person(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }
    
    class Foo {
     
        private IMethods methods;
        
        public Foo(IMethods methods) {
            this.methods = methods;
        }

        public void bar() {
            Person person = new Person(12);
            methods.simpleMethod(person);
        }
    }

    private Object capture(Capture<Person> capturedArgument) {
        Mockito.argThat(capturedArgument);
        return null;
    }

    @Mock IMethods mock;

    //TODO: after 1.7 decide on getting capture matcher into Mockito
    @Test
    public void shouldUseArgumentCaptorInEasyMockStyle() {
        Foo foo = new Foo(mock);
        foo.bar();
        Capture<Person> capturedArgument = new Capture<Person>();
        verify(mock).simpleMethod(capture(capturedArgument));
        assertEquals(12, capturedArgument.getValue().getAge());
    }
    
    @Ignore("assertion idea is not for current release I guess")
    @Test
    public void shouldUseAssertor() {
        Foo foo = new Foo(mock);
        foo.bar();
        verify(mock).simpleMethod(argThat(new Assertion<Person>() {
            public void asserts(Person person) {
                assertEquals(12, person.getAge());
            }
        }));
    }
    
    public static <T> T argThat(Assertion<T> assertion) {
        return null;
    }
    
    public interface Assertion<T> {
        void asserts(T object);
    }
}