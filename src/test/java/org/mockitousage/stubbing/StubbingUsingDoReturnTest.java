/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitousage.MethodsImpl;
import org.mockitoutil.TestBase;

import java.io.IOException;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("serial")
public class StubbingUsingDoReturnTest extends TestBase {

    @Mock private IMethods mock;
    
    @After public void resetState() {
        super.resetState();
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
    
    class FooRuntimeException extends RuntimeException {}
    
    @Test
    public void shouldStubWithThrowable() throws Exception {
        doThrow(new FooRuntimeException()).when(mock).voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (FooRuntimeException e) {}
    }
    
    @Test
    public void shouldAllowSettingValidCheckedException() throws Exception {
        doThrow(new IOException()).when(mock).throwsIOException(0);
        
        try {
            mock.throwsIOException(0);
            fail();
        } catch (IOException e) {}
    }
    
    class FooCheckedException extends Exception {}
    
    @Test
    public void shouldDetectInvalidCheckedException() throws Exception {
        try {
            doThrow(new FooCheckedException()).when(mock).throwsIOException(0);
            fail();
        } catch (Exception e) {
            assertContains("Checked exception is invalid", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenReturnSetForVoid() throws Exception {
        try {
            doReturn("foo").when(mock).voidMethod();
            fail();
        } catch (MockitoException e) {
            assertContains("void method", e.getMessage());
            assertContains("cannot", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenNotAMockPassed() throws Exception {
        try {
            doReturn("foo").when("foo").toString();
            fail();
        } catch (Exception e) {
            assertContains("Argument passed to when() is not a mock", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenNullPassed() throws Exception {
        try {
            doReturn("foo").when((Object) null).toString();
            fail();
        } catch (Exception e) {
            assertContains("Argument passed to when() is null", e.getMessage());
        }
    }    
    
    @Test
    public void shouldAllowChainedStubbing() {
        doReturn("foo").
        doThrow(new RuntimeException()).
        doReturn("bar")
        .when(mock).simpleMethod();
        
        assertEquals("foo", mock.simpleMethod());
        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException e) {}
        
        assertEquals("bar", mock.simpleMethod());
        assertEquals("bar", mock.simpleMethod());
    }

    @Test
    public void shouldAllowDoCallRealMethodInChainedStubbing() throws Exception {
        MethodsImpl methods = mock(MethodsImpl.class);
        doReturn("A").doCallRealMethod()
                .when(methods).simpleMethod();

        assertEquals("A", methods.simpleMethod());
        assertEquals(null, methods.simpleMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldAllowChainedStubbingWithExceptionClass() throws Exception {
        doReturn("whatever").doThrow(IllegalArgumentException.class).when(mock).simpleMethod();

        assertEquals("whatever", mock.simpleMethod());
        mock.simpleMethod();
    }

    @Test
    public void shouldAllowChainedStubbingOnVoidMethods() {
        doNothing().
        doNothing().
        doThrow(new RuntimeException())
        .when(mock).voidMethod();
        
        mock.voidMethod();
        mock.voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (RuntimeException e) {}
        try {
            mock.voidMethod();
            fail();
        } catch (RuntimeException e) {}
    }
    
    @Test
    public void shouldStubWithGenericAnswer() {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "foo";
            }
        })
        .when(mock).simpleMethod();
        
        assertEquals("foo", mock.simpleMethod());
    }
    
    @Test
    public void shouldNotAllowDoNothingOnNonVoids() {
        try {
            doNothing().when(mock).simpleMethod();
            fail();
        } catch (MockitoException e) {
            assertContains("Only void methods can doNothing()", e.getMessage());
        }
    }
    
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
 
    @Test
    public void shouldDetectInvalidReturnType() throws Exception {
        try {
            doReturn("foo").when(mock).booleanObjectReturningMethod();
            fail();
        } catch (Exception e) {
            assertContains("String cannot be returned by booleanObjectReturningMethod()" +
                    "\n" +
                    "booleanObjectReturningMethod() should return Boolean",
                    e.getMessage());
        }
    }
    
    @Test
    public void shouldDetectWhenNullAssignedToBoolean() throws Exception {
        try {
            doReturn(null).when(mock).intReturningMethod();
            fail();
        } catch (Exception e) {
            assertContains("null cannot be returned by intReturningMethod", e.getMessage());
        }
    }
    
    @Test
    public void shouldAllowStubbingWhenTypesMatchSignature() throws Exception {
        doReturn("foo").when(mock).objectReturningMethodNoArgs();
        doReturn("foo").when(mock).simpleMethod();
        doReturn(1).when(mock).intReturningMethod();
        doReturn(new Integer(2)).when(mock).intReturningMethod();
    }
}
