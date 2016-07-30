/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SmartNullsStubbingTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class, Mockito.RETURNS_SMART_NULLS);
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
            assertThat(e).hasMessageContaining("unstubbedMethodInvokedHere(");
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

        Bar getBarWithParams(int x, String y) {
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

    @Test
    public void shouldShowParameters() {
        Foo foo = mock(Foo.class, RETURNS_SMART_NULLS);
        Bar smartNull = foo.getBarWithParams(10, "yes sir");

        try {
            smartNull.boo();
            fail();
        } catch (Exception e) {
            assertThat(e).hasMessageContaining("yes sir");
        }
    }

    @Test
    public void shouldShowParametersWhenParamsAreHuge() {
        Foo foo = mock(Foo.class, RETURNS_SMART_NULLS);
        String longStr = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        Bar smartNull = foo.getBarWithParams(10, longStr);

        try {
            smartNull.boo();
            fail();
        } catch (Exception e) {
            assertThat(e).hasMessageContaining("Lorem Ipsum");
        }
    }
}