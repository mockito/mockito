/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

public class PlaygroundTest extends TestBase {
    static class Foo {
        
        int doSomeThing() {
            return 0;
        }

        protected String getStuff() {
            return "foo";
        }
    }
    
    class Boo {
        final public Object withLong(long y) {
                         return "";
        }

        public Object foo() {
                   return "";
        }
    }

    Foo mock;
    @Mock IMethods mockTwo;
    
    @Test
    public void spyInAction() {

    }
    
    @Test
    public void partialMockInAction() {
//        mock = mock(Foo.class, withSettings() 
//            .defaultBehavior(CALLS_REAL_METHODS);

//        mock = mock(Foo.class, withSettings() 
//            .defaultMockAnswer(CALLS_REAL_METHODS);

//        mock = mock(Foo.class, withSettings() 
//            .defaultAnswer(CALLS_REAL_METHODS);

//        mock = mock(Foo.class, CALLS_REAL_METHODS);

//        mock = mock(Foo.class, withSettings() 
//            .defaultBehavior(CALLS_REAL_METHODS)
//            .createUsingDefaultConstructor();
//        
//        mock = mock(Foo.class, withSettings() 
//            .defaultBehavior(CALLS_REAL_METHODS)
//            .createPassingArguments("some arg", 1);
//
//        spy = spy(Foo.class, "some arg", 1);
//        
//            .withName("foo")
//            .withDefaultBehavior(RETURNS_SMART_NULLS)
//            .withInterfaces(Bar.class);
//        
//        mock = mock(Foo.class)
//            .name("foo")
//            .defaultBehavior(RETURNS_SMART_NULLS)
//            .interfaces(Bar.class);
//        
//        mock = mock(Foo.class)
//            .named("foo")
//            .byDefault(RETURNS_SMART_NULLS)
//            .alsoImplements(Bar.class, Bar2.class);
//        
//        mock = mock(Foo.class)
//            hasName("foo");
        
//        when(mock.getStuff()).thenReturn("aha!");
//        when(mock.doSomeThing()).thenCallRealMethod();
//
        
//        mock.doSomeThing();
    }
    
//    interface Colored {
//        
//    }
//    
//    interface Bar {
//        <T extends Foo & Colored> T getColoredPoint();
//    }
//    
//    @Test
//    public void testname() throws Exception {
//        when(mock.get()).then(returnArgument());
//        
//        Bar mock = mock(Bar.class);
//        when(mock.getColoredPoint()).thenReturn(new Foo());
//    }
}