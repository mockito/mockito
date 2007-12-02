/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.collectionContaining;

import java.util.Arrays;

import org.junit.*;
import org.mockito.exceptions.VerificationError;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class BasicStubbingTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class);
    }
    
    @Test
    public void shouldStubAllMethodsByDefault() throws Exception {
        assertEquals(0, mock.intReturningMethod(1));
        assertEquals(0, mock.intReturningMethod(100));
        
        assertNull(mock.objectReturningMethod(1));
        assertNull(mock.objectReturningMethod(100));
        
        assertTrue(mock.listReturningMethod(1, 100).isEmpty());
        assertTrue(mock.listReturningMethod().isEmpty());
    }
    
    @Test
    public void shouldStubAndLetBeCalledAnyTimes() throws Exception {
        stub(mock.intReturningMethod(14)).andReturn(14);
        
        assertEquals(14, mock.intReturningMethod(14));
        assertEquals(14, mock.intReturningMethod(14));
        
        stub(mock.listReturningMethod()).andReturn(Arrays.asList("elementOne", "elementTwo"));
        
        assertThat(mock.listReturningMethod(), collectionContaining("elementOne", "elementTwo"));
        assertThat(mock.listReturningMethod(), collectionContaining("elementOne", "elementTwo"));
        
        stub(mock.objectReturningMethod(10)).andReturn("test");
        
        assertEquals("test", mock.objectReturningMethod(10));
        assertEquals("test", mock.objectReturningMethod(10));
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
    public void shouldStubbingWithThrowableFailVerification() {
        stub(mock.simpleMethod("one")).andThrows(new RuntimeException());
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