/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Mockito.*;
import static org.mockitoutil.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class SmartNullsStubbingTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class, Mockito.SMART_NULLS);
    }
    
    public IMethods unstubbedMethodInvokedHere(IMethods mock) {
        return mock.iMethodsReturningMethod();
    }

    @Test
    public void shouldSmartNPEPointToUnstubbedCall() throws Exception {
        IMethods methods = unstubbedMethodInvokedHere(mock); 
        try {
            methods.simpleMethod();
            fail();
        } catch (SmartNullPointerException e) {
            assertThat(e.getCause(),  hasMethodInStackTraceAt(0, "unstubbedMethodInvokedHere"));
        }
    }

    interface Bar {
        void boo();
    }
    
    class Foo {
        Foo getSomeClass() {
            return null;
        }
        
        Bar getSomeInterface() {
            return null;
        }
        
        void boo() {}
    }
    
    @Test
    public void shouldThrowSmartNPEWhenMethodReturnsClass() throws Exception {
        Foo mock = mock(Foo.class, SMART_NULLS);
        Foo foo = mock.getSomeClass(); 
        try {
            foo.boo();
            fail();
        } catch (SmartNullPointerException e) {}
    }
    
    
    @Test
    public void shouldThrowSmartNPEWhenMethodReturnsInterface() throws Exception {
        Foo mock = mock(Foo.class, SMART_NULLS);
        Bar bar = mock.getSomeInterface(); 
        try {
            bar.boo();
            fail();
        } catch (SmartNullPointerException e) {}
    }
}