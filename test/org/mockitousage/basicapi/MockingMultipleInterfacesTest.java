/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class MockingMultipleInterfacesTest extends TestBase {

    class Foo {}
    interface IFoo {}
    interface IBar {}
    
    @Test
    public void shouldAllowMultipleInterfaces() {
        //when
        Foo mock = mock(Foo.class, withSettings().extraInterfaces(IFoo.class, IBar.class));
        
        //then
        assertThat(mock, is(IFoo.class));
        assertThat(mock, is(IBar.class));
    }
    
    @Test
    public void shouldScreamWhenNullPassedInsteadOfAnInterface() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces(IFoo.class, null));
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("extraInterfaces() does not accept null parameters", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenNoArgsPassed() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces());
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("extraInterfaces() requires at least one interface", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenNullPassedInsteadOfAnArray() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces((Class[]) null));
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("extraInterfaces() requires at least one interface", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenNonInterfacePassed() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces(Foo.class));
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("Foo which is not an interface", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenTheSameInterfacesPassed() {
        try {
            //when
            mock(IMethods.class, withSettings().extraInterfaces(IMethods.class));
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("You mocked following type: IMethods", e.getMessage());
        }
    }
}