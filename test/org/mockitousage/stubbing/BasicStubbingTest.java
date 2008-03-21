/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class BasicStubbingTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class);
    }
    
    @Test
    public void shouldEvaluateLatestStubbingFirst() throws Exception {
        stub(mock.objectReturningMethod(isA(Integer.class))).toReturn(100);
        stub(mock.objectReturningMethod(200)).toReturn(200);
        
        assertEquals(200, mock.objectReturningMethod(200));
        assertEquals(100, mock.objectReturningMethod(666));
        assertEquals("default behavior should return null", null, mock.objectReturningMethod("blah"));
    }
    
    @Ignore("prototyping new API")
    @Test
    public void shouldReturnMultipleValues() throws Exception {
//        stub(mock.simpleMethod())
//            .toReturn("test")
//            .andThen("test2")
//            .andThen(null);
    }
    
    @Test
    public void shouldStubbingBeTreatedAsInteraction() throws Exception {
        stub(mock.booleanReturningMethod(1)).toReturn(true);
        
        mock.booleanReturningMethod(1);
        
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
        //TODO can I somehow avoid a cast here:
        stub(mockTwo.getList()).toReturn((List) subs);
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