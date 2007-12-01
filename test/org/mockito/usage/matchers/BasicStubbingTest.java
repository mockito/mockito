/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.matchers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.collectionContaining;

import java.util.Arrays;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.VerificationError;
import org.mockito.usage.IMethods;

@SuppressWarnings("unchecked")
public class BasicStubbingTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    @Test
    public void shouldStubAllMethodsByDefault() throws Exception {
        assertEquals(0, mock.intReturningMethod(1));
        assertEquals(0, mock.intReturningMethod(2));
        
        assertNull(mock.objectReturningMethod(1));
        assertNull(mock.objectReturningMethod(2));
        
        assertEquals(0, mock.listReturningMethod().size());
        assertEquals(0, mock.listReturningMethod().size());
    }
    
    @Test
    public void shouldStubAndLetBeCalledAnyTimes() throws Exception {
        stub(mock.intReturningMethod(14)).andReturn(14);
        
        assertEquals(14, mock.intReturningMethod(14));
        assertEquals(14, mock.intReturningMethod(14));
        
        stub(mock.listReturningMethod("x")).andReturn(Arrays.asList("elementOne", "elementTwo"));
        
        assertThat(mock.listReturningMethod("x"), collectionContaining("elementOne", "elementTwo"));
        assertThat(mock.listReturningMethod("x"), collectionContaining("elementOne", "elementTwo"));
        
        stub(mock.objectReturningMethod(100)).andReturn("hundred");
        
        assertEquals("hundred", mock.objectReturningMethod(100));
        assertEquals("hundred", mock.objectReturningMethod(100));
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
}
