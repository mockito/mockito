/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.spies;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SpyingOnRealObjectsTest extends TestBase {

    List<String> list = new LinkedList<String>();
    List<String> spy = Mockito.spy(list);
    
    @Test
    public void shouldVerify() {
        spy.add("one");
        spy.add("two");

        assertEquals("one", spy.get(0));
        assertEquals("two", spy.get(1));

        verify(spy).add("one");
        verify(spy).add("two");
    }

    @Test
    public void shouldBeAbleToMockObjectBecauseWhyNot() {
        spy(new Object());
    }
    
    @Test
    public void shouldStub() {
        spy.add("one");
        when(spy.get(0))
            .thenReturn("1")
            .thenReturn("1 again");
               
        assertEquals("1", spy.get(0));
        assertEquals("1 again", spy.get(0));
        assertEquals("one", spy.iterator().next());
        
        assertEquals(1, spy.size());
    }
    
    @Test
    public void shouldAllowOverridingStubs() {
        when(spy.contains(anyObject())).thenReturn(true);
        when(spy.contains("foo")).thenReturn(false);
        
        assertTrue(spy.contains("bar"));
        assertFalse(spy.contains("foo"));
    }
    
    @Test
    public void shouldStubVoid() {
        doNothing()
        .doThrow(new RuntimeException())
        .when(spy)
        .clear();

        spy.add("one");
        spy.clear();
        try {
            spy.clear();
            fail();
        } catch (RuntimeException e) {}
            
        assertEquals(1, spy.size());
    }
    
    @Test
    public void shouldStubWithDoReturnAndVerify() {
        doReturn("foo")
        .doReturn("bar")
        .when(spy).get(0);
        
        assertEquals("foo", spy.get(0));
        assertEquals("bar", spy.get(0));
        
        verify(spy, times(2)).get(0);
        verifyNoMoreInteractions(spy);
    }
    
    @Test
    public void shouldVerifyInOrder() {
        spy.add("one");
        spy.add("two");
        
        InOrder inOrder = inOrder(spy);
        inOrder.verify(spy).add("one");
        inOrder.verify(spy).add("two");
        
        verifyNoMoreInteractions(spy);
    }
    
    @Test
    public void shouldVerifyInOrderAndFail() {
        spy.add("one");
        spy.add("two");
        
        InOrder inOrder = inOrder(spy);
        inOrder.verify(spy).add("two");
        try {
            inOrder.verify(spy).add("one");
            fail();
        } catch (VerificationInOrderFailure f) {}
    }
    
    @Test
    public void shouldVerifyNumberOfTimes() {
        spy.add("one");
        spy.add("one");
        
        verify(spy, times(2)).add("one");
        verifyNoMoreInteractions(spy);
    }
    
    @Test
    public void shouldVerifyNumberOfTimesAndFail() {
        spy.add("one");
        spy.add("one");
        
        try {
            verify(spy, times(3)).add("one");
            fail();
        } catch (TooLittleActualInvocations e) {}
    }
    
    @Test
    public void shouldVerifyNoMoreInteractionsAndFail() {
        spy.add("one");
        spy.add("two");
        
        verify(spy).add("one");
        try {
            verifyNoMoreInteractions(spy);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void shouldToString() {
        spy.add("foo");
        assertEquals("[foo]" , spy.toString());
    }
    
    interface Foo {
        String print();
    }
    
    @Test
    public void shouldAllowSpyingAnonymousClasses() {
        //when
        Foo spy = spy(new Foo() {
            public String print() {
                return "foo";
            }
        });

        //then
        assertEquals("foo", spy.print());
    }
    
    @Test
    public void shouldSayNiceMessageWhenSpyingOnPrivateClass() throws Exception {
        List<String> real = Arrays.asList("first", "second");
        try {
            spy(real);
            fail();
        } catch (MockitoException e) {
            assertThat(e).hasMessageContaining("Most likely it is a private class that is not visible by Mockito");
        }
    }
}