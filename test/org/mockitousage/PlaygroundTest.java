/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

public class PlaygroundTest extends TestBase {
    static class Foo {
        
        int doSomeThing() {
            System.out.println(getStuff());
            return 0;
        }

        protected String getStuff() {
            return "foo";
        }
    }

    Foo mock;
    @Mock IMethods mockTwo;
    
    @Ignore
    @Test
    public void spyInAction() {
        mock = spy(new Foo());
// mock = spy(Foo.class, new Konstructor() {} );
// mock = spy(Foo.class, Konstructor.guess());
// mock = spy(Foo.class, Konstructor.withArguments("1", "2"));
        
        when(mock.getStuff()).thenReturn("aha!");
        
        mock.doSomeThing();
    }
    
    @Test
    public void partialMockInAction() {
//        mock = mock(Foo.class, withSettings() 
//            .defaultBehavior(CALLS_REAL_METHODS)
//            .createUsing(constructor, 1, "foo");
//
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