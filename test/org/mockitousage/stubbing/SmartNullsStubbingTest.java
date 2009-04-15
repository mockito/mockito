/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class SmartNullsStubbingTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class, Mockito.RETURNS_SMART_NULLS);
        super.makeStackTracesClean();
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
            assertContains("unstubbedMethodInvokedHere(", e.getMessage());
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
        Foo mock = mock(Foo.class, RETURNS_SMART_NULLS);
        Foo foo = mock.getSomeClass(); 
        try {
            foo.boo();
            fail();
        } catch (SmartNullPointerException e) {}
    }
    
    @Test
    public void shouldThrowSmartNPEWhenMethodReturnsInterface() throws Exception {
        Foo mock = mock(Foo.class, RETURNS_SMART_NULLS);
        Bar bar = mock.getSomeInterface(); 
        try {
            bar.boo();
            fail();
        } catch (SmartNullPointerException e) {}
    }
    
    
    @Test
    public void shouldReturnOrdinaryEmptyValuesForOrdinaryTypes() throws Exception {
        IMethods mock = mock(IMethods.class, RETURNS_SMART_NULLS);
        
        assertEquals("", mock.stringReturningMethod());
        assertEquals(0, mock.intReturningMethod());
        assertEquals(true, mock.listReturningMethod().isEmpty());
        assertEquals(0, mock.arrayReturningMethod().length);
    }
    
    @Test
    public void shouldNotThrowSmartNullPointerOnToString() {
        Object smartNull = mock.objectReturningMethod();
        try {
            verify(mock).simpleMethod(smartNull);
            fail();
        } catch (WantedButNotInvoked e) {}
    }

    @Test
    public void shouldNotThrowSmartNullPointerOnObjectMethods() {
        Object smartNull = mock.objectReturningMethod();
        smartNull.toString();
    }
}