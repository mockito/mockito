/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.mockito.CrazyMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.VerificationError;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")  
public class VerificationUsingMatchersTest {
    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void shouldVerifyUsingSameMatcher() {
        Object one = new String("1243");
        Object two = new String("1243");
        Object three = new String("1243");

        assertNotSame(one, two);
        assertEquals(one, two);
        assertEquals(two, three);

        mock.oneArg(one);
        mock.oneArg(two);
        
        verify(mock).oneArg(CrazyMatchers.same(one));
        verify(mock, 2).oneArg(two);
        
        try {
            verify(mock).oneArg(CrazyMatchers.same(three));
            fail();
        } catch (VerificationError e) {}
    }  
    
    @Test
    public void shouldVerifyUsingMixedMatchers() {
        mock.threeArgumentMethod(11, "", "01234");

        try {
            verify(mock).threeArgumentMethod(and(geq(7), leq(10)), isA(String.class), contains("123"));
            fail();
        } catch (VerificationError e) {}

        mock.threeArgumentMethod(8, new Object(), "01234");
        
        try {
            verify(mock).threeArgumentMethod(and(geq(7), leq(10)), isA(String.class), contains("123"));
            fail();
        } catch (VerificationError e) {}
        
        mock.threeArgumentMethod(8, "", "no match");

        try {
            verify(mock).threeArgumentMethod(and(geq(7), leq(10)), isA(String.class), contains("123"));
            fail();
        } catch (VerificationError e) {}
        
        mock.threeArgumentMethod(8, "", "123");
        
        verify(mock).threeArgumentMethod(and(geq(7), leq(10)), isA(String.class), contains("123"));
    }
}