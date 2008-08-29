/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class BasicStubbingTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class);
    }
    
    @Test
    public void shouldEvaluateLatestStubbingFirst() throws Exception {
        when(mock.objectReturningMethod(isA(Integer.class))).thenReturn(100);
        when(mock.objectReturningMethod(200)).thenReturn(200);
        
        assertEquals(200, mock.objectReturningMethod(200));
        assertEquals(100, mock.objectReturningMethod(666));
        assertEquals("default behavior should return null", null, mock.objectReturningMethod("blah"));
    }
    
    @Test
    public void shouldStubbingBeTreatedAsInteraction() throws Exception {
        when(mock.booleanReturningMethod()).thenReturn(true);
        
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
        when(mockTwo.getList()).thenReturn((List) subs);
    }
    
    @Test
    public void shouldAllowStubbingToString() throws Exception {
        IMethods mockTwo = mock(IMethods.class);
        when(mockTwo.toString()).thenReturn("test");
        
        assertThat(mock.toString(), contains("Mock for IMethods"));
        assertEquals("test", mockTwo.toString());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void shouldStubbingWithThrowableFailVerification() {
        when(mock.simpleMethod("one")).thenThrow(new RuntimeException());
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