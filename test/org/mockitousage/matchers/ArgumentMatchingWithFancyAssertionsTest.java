/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ArgumentMatchingWithFancyAssertionsTest extends TestBase {

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

    @Mock IMethods mock;

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