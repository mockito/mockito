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
    public void should_stub() throws Exception {
        doReturn("foo").when(mock).simpleMethod();
        doReturn("bar").when(mock).simpleMethod();
        
        assertEquals("bar", mock.simpleMethod());
    }
    
    @Test
    public void should_stub_with_args() throws Exception {
        doReturn("foo").when(mock).simpleMethod("foo");
        doReturn("bar").when(mock).simpleMethod(eq("one"), anyInt());
        
        assertEquals("foo", mock.simpleMethod("foo"));
        assertEquals("bar", mock.simpleMethod("one", 234));
        assertEquals(null, mock.simpleMethod("xxx", 234));
    }
    
    class FooRuntimeException extends RuntimeException {}
    class FooRuntimeExceptionTwo extends RuntimeException {}
    
    @Test
    public void should_stub_with_throwable() throws Exception {
        doThrow(new FooRuntimeException()).when(mock).voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (FooRuntimeException e) {}
    }



    @Test(expected = FooRuntimeExceptionTwo.class)
    public void should_stub_with_multiple_throwables() throws Exception {
        doThrow(new FooRuntimeException(),new FooRuntimeExceptionTwo()).when(mock).voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (FooRuntimeException e) {}

        mock.voidMethod();
    }

    @Test
    public void should_stub_with_multiple_throwables_after_first() throws Exception {
        doThrow(new FooRuntimeException(),new FooRuntimeExceptionTwo()).when(mock).voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (FooRuntimeException e) {}
        for (int i= 0; i<5 ; ++i){
            try {
                mock.voidMethod();
                fail();
            } catch (FooRuntimeExceptionTwo e) {}
        }
    }
    
    @Test
    public void should_allow_setting_valid_checked_exception() throws Exception {
        doThrow(new IOException()).when(mock).throwsIOException(0);
        
        try {
            mock.throwsIOException(0);
            fail();
        } catch (IOException e) {}
    }
    
    class FooCheckedException extends Exception {}
    
    @Test
    public void should_detect_invalid_checked_exception() throws Exception {
        try {
            doThrow(new FooCheckedException()).when(mock).throwsIOException(0);
            fail();
        } catch (Exception e) {
            assertContains("Checked exception is invalid", e.getMessage());
        }
    }
    
    @Test
    public void should_scream_when_return_set_for_void() throws Exception {
        try {
            doReturn("foo").when(mock).voidMethod();
            fail();
        } catch (MockitoException e) {
            assertContains("void method", e.getMessage());
            assertContains("cannot", e.getMessage());
        }
    }
    
    @Test
    public void should_scream_when_not_a_mock_passed() throws Exception {
        try {
            doReturn("foo").when("foo").toString();
            fail();
        } catch (Exception e) {
            assertContains("Argument passed to when() is not a mock", e.getMessage());
        }
    }
    
    @Test
    public void should_scream_when_null_passed() throws Exception {
        try {
            doReturn("foo").when((Object) null).toString();
            fail();
        } catch (Exception e) {
            assertContains("Argument passed to when() is null", e.getMessage());
        }
    }    
    
    @Test
    public void should_allow_chained_stubbing() {
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
    public void should_allow_do_call_real_method_in_chained_stubbing() throws Exception {
        MethodsImpl methods = mock(MethodsImpl.class);
        doReturn("A").doCallRealMethod()
                .when(methods).simpleMethod();

        assertEquals("A", methods.simpleMethod());
        assertEquals(null, methods.simpleMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_allow_chained_stubbing_with_exception_class() throws Exception {
        doReturn("whatever").doThrow(IllegalArgumentException.class).when(mock).simpleMethod();

        assertEquals("whatever", mock.simpleMethod());
        mock.simpleMethod();
    }

    @Test
    public void should_allow_chained_stubbing_on_void_methods() {
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
    public void should_stub_with_generic_answer() {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "foo";
            }
        })
        .when(mock).simpleMethod();
        
        assertEquals("foo", mock.simpleMethod());
    }
    
    @Test
    public void should_not_allow_do_nothing_on_non_voids() {
        try {
            doNothing().when(mock).simpleMethod();
            fail();
        } catch (MockitoException e) {
            assertContains("Only void methods can doNothing()", e.getMessage());
        }
    }
    
    @Test
    public void should_stubbing_be_treated_as_interaction() throws Exception {
        doReturn("foo").when(mock).simpleMethod();
        mock.simpleMethod();
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void should_verify_stubbed_call() throws Exception {
        doReturn("foo").when(mock).simpleMethod();
        mock.simpleMethod();
        mock.simpleMethod();
        
        verify(mock, times(2)).simpleMethod();
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void should_allow_stubbing_to_string() throws Exception {
        doReturn("test").when(mock).toString();
        assertEquals("test", mock.toString());
    }
 
    @Test
    public void should_detect_invalid_return_type() throws Exception {
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
    public void should_detect_when_null_assigned_to_boolean() throws Exception {
        try {
            doReturn(null).when(mock).intReturningMethod();
            fail();
        } catch (Exception e) {
            assertContains("null cannot be returned by intReturningMethod", e.getMessage());
        }
    }
    
    @Test
    public void should_allow_stubbing_when_types_match_signature() throws Exception {
        doReturn("foo").when(mock).objectReturningMethodNoArgs();
        doReturn("foo").when(mock).simpleMethod();
        doReturn(1).when(mock).intReturningMethod();
        doReturn(new Integer(2)).when(mock).intReturningMethod();
    }
}
