/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class StubbingUsingDoReturnTest extends TestBase {

    @Mock private IMethods mock;

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
    
    @Ignore
    @Test
    public void shouldStubWithThrowable() throws Exception {
        doThrow(new FooException()).when(mock).voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (FooException e) {}
    }
    
    @Ignore
    @Test
    public void shouldScreamWhenReturnSetForVoid() throws Exception {
        doReturn(new RuntimeException()).when(mock).voidMethod();
        fail();
    }
    
    @Ignore
    @Test
    public void shouldScreamWhenNotAMockPassed() throws Exception {
        try {
            doReturn("foo").when("foo").toString();
            fail();
        } catch (Exception e) {
            assertEquals("Argument passed to when() method is not a mock", e.getMessage());
        }
    }
    
    //TODO when not a mock passed
    
    //TODO checked Exceptions
    
    //TODO chains
    
    //TODO state validation
    
    //TODO should verify
    
    //TODO exception messages with UnfinishedStubbingException
    
    @Test
    public void shouldStubbingBeTreatedAsInteraction() throws Exception {
        stub(mock.booleanReturningMethod()).toReturn(true);
        
        mock.booleanReturningMethod();
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    class Base {}
    class Sub extends Base {}

    interface Generic {
        List<Base> getList();
    }
    
    @Test
    public void shouldAllowStubbingWithSubtypes() throws Exception {
        Generic mockTwo = mock(Generic.class);
        
        List<Sub> subs = null;
        //can I somehow avoid a cast here:
        stub(mockTwo.getList()).toReturn((List) subs);
    }
    
    @Test
    public void shouldAllowStubbingToString() throws Exception {
        IMethods mockTwo = mock(IMethods.class);
        stub(mockTwo.toString()).toReturn("test");
        
        assertThat(mock.toString(), contains("Mock for IMethods"));
        assertEquals("test", mockTwo.toString());
    }
    
    @Test
    public void shouldStubbingWithThrowableFailVerification() {
        stub(mock.simpleMethod("one")).toThrow(new RuntimeException());
        stubVoid(mock).toThrow(new RuntimeException()).on().simpleMethod("two");
        
        verifyZeroInteractions(mock);
        
        mock.simpleMethod("foo");
        
        try {
            verify(mock).simpleMethod("one");
            fail();
        } catch (ArgumentsAreDifferent e) {}
        
        try {
            verify(mock).simpleMethod("two");
            fail();
        } catch (ArgumentsAreDifferent e) {}
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
}