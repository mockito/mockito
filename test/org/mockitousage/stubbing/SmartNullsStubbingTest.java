/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    }
    
    public IMethods unstubbedMethodInvokedHere(final IMethods mock) {
        return mock.iMethodsReturningMethod();
    }

    @Test
    public void shouldSmartNPEPointToUnstubbedCall() throws Exception {
        final IMethods methods = unstubbedMethodInvokedHere(mock); 
        try {
            methods.simpleMethod();
            fail();
        } catch (final SmartNullPointerException e) {
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

        Bar getBarWithParams(final int x, final String y) {
            return null;
        }

        void boo() {}
    }

    @Test
    public void shouldThrowSmartNPEWhenMethodReturnsClass() throws Exception {
        final Foo mock = mock(Foo.class, RETURNS_SMART_NULLS);
        final Foo foo = mock.getSomeClass();
        try {
            foo.boo();
            fail();
        } catch (final SmartNullPointerException e) {}
    }

    @Test
    public void shouldThrowSmartNPEWhenMethodReturnsInterface() throws Exception {
        final Foo mock = mock(Foo.class, RETURNS_SMART_NULLS);
        final Bar bar = mock.getSomeInterface();
        try {
            bar.boo();
            fail();
        } catch (final SmartNullPointerException e) {}
    }


    @Test
    public void shouldReturnOrdinaryEmptyValuesForOrdinaryTypes() throws Exception {
        final IMethods mock = mock(IMethods.class, RETURNS_SMART_NULLS);

        assertEquals("", mock.stringReturningMethod());
        assertEquals(0, mock.intReturningMethod());
        assertEquals(true, mock.listReturningMethod().isEmpty());
        assertEquals(0, mock.arrayReturningMethod().length);
    }

    @Test
    public void shouldNotThrowSmartNullPointerOnToString() {
        final Object smartNull = mock.objectReturningMethod();
        try {
            verify(mock).simpleMethod(smartNull);
            fail();
        } catch (final WantedButNotInvoked e) {}
    }

    @Test
    public void shouldNotThrowSmartNullPointerOnObjectMethods() {
        final Object smartNull = mock.objectReturningMethod();
        smartNull.toString();
    }

    @Test
    public void shouldShowParameters() {
        final Foo foo = mock(Foo.class, RETURNS_SMART_NULLS);
        final Bar smartNull = foo.getBarWithParams(10, "yes sir");

        try {
            smartNull.boo();
            fail();
        } catch (final Exception e) {
            assertContains("yes sir", e.getMessage());
        }
    }

    @Test
    public void shouldShowParametersWhenParamsAreHuge() {
        final Foo foo = mock(Foo.class, RETURNS_SMART_NULLS);
        final String longStr = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        final Bar smartNull = foo.getBarWithParams(10, longStr);

        try {
            smartNull.boo();
            fail();
        } catch (final Exception e) {
            assertContains("Lorem Ipsum", e.getMessage());
        }
    }
}