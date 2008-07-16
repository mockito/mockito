/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Test;
import org.mockito.StateMaster;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class StubbingUsingDoReturnTest extends TestBase {

    @Mock private IMethods mock;
    
    @After public void resetState() {
        StateMaster.reset();
    }

    @Test
    public void shouldStub() throws Exception {
        doReturn("foo").when(mock).simpleMethod();
        doReturn("bar").when(mock).simpleMethod();
        
        assertEquals("bar", mock.simpleMethod());
    }
    
    @Test
    public void shouldStubWithArgs() throws Exception {
        doReturn("foo").when(mock).simpleMethod("foo");
        doReturn("bar").when(mock).simpleMethod(eq("one"), anyInt());
        
        assertEquals("foo", mock.simpleMethod("foo"));
        assertEquals("bar", mock.simpleMethod("one", 234));
        assertEquals(null, mock.simpleMethod("xxx", 234));
    }
    
    @SuppressWarnings("serial")
    class FooException extends RuntimeException {}
    
    @Test
    public void shouldStubWithThrowable() throws Exception {
        doThrow(new FooException()).when(mock).voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (FooException e) {}
    }
    
    @Test
    public void shouldScreamWhenReturnSetForVoid() throws Exception {
        try {
            doReturn("foo").when(mock).voidMethod();
            fail();
        } catch (MockitoException e) {
            //TODO could the message be clearer?
            assertThat(e.getMessage(), contains("Cannot stub a void method with a return value"));
        }
    }
    
    @Test
    public void shouldScreamWhenNotAMockPassed() throws Exception {
        try {
            doReturn("foo").when("foo").toString();
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), contains("Argument passed to when() is not a mock"));
        }
    }
    
    @Test
    public void shouldScreamWhenNullPassed() throws Exception {
        try {
            doReturn("foo").when(null).toString();
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), contains("Argument passed to when() is null"));
        }
    }    
    
    //TODO checked Exceptions
    
    //TODO chains
    
    //TODO state validation
    
    //TODO should verify
    
    //TODO exception messages with UnfinishedStubbingException
    
    @Test
    public void shouldStubbingBeTreatedAsInteraction() throws Exception {
        doReturn("foo").when(mock).simpleMethod();
        mock.simpleMethod();
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void shouldVerifyStubbedCall() throws Exception {
        doReturn("foo").when(mock).simpleMethod();
        mock.simpleMethod();
        mock.simpleMethod();
        
        verify(mock, times(2)).simpleMethod();
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldAllowStubbingToString() throws Exception {
        doReturn("test").when(mock).toString();
        assertEquals("test", mock.toString());
    }
}