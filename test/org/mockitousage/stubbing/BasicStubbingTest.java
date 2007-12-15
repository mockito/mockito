/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.stubVoid;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.verification.VerificationError;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class BasicStubbingTest extends RequiresValidState {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class);
    }
    
    @Test
    public void shouldEvaluateLatestStubbingFirst() throws Exception {
        stub(mock.objectReturningMethod(isA(Integer.class))).andReturn(100);
        stub(mock.objectReturningMethod(200)).andReturn(200);
        
        assertEquals(200, mock.objectReturningMethod(200));
        assertEquals(100, mock.objectReturningMethod(666));
        assertEquals("default behavior should return null", null, mock.objectReturningMethod("blah"));
    }
    
    @Test
    public void shouldStubbingBeTreatedAsInteraction() throws Exception {
        stub(mock.booleanReturningMethod(1)).andReturn(true);
        
        mock.booleanReturningMethod(1);
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldStubbingWithThrowableFailVerification() {
        stub(mock.simpleMethod("one")).andThrow(new RuntimeException());
        stubVoid(mock).toThrow(new RuntimeException()).on().simpleMethod("two");
        
        verifyZeroInteractions(mock);
        
        mock.simpleMethod("foo");
        
        try {
            verify(mock).simpleMethod("one");
            fail();
        } catch (VerificationError e) {}
        
        try {
            verify(mock).simpleMethod("two");
            fail();
        } catch (VerificationError e) {}
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (VerificationError e) {}
    }
}