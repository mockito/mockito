/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.stubbing.answers.ReturnsElementsOf;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class StubbingWithExtraAnswersTest extends TestBase {

    @Mock private IMethods mock;
    
    @Test
    public void shouldWorkAsStandardMockito() throws Exception {
        //when
        List<Integer> list = asList(1, 2, 3);
        when(mock.objectReturningMethodNoArgs()).thenAnswer(new ReturnsElementsOf(list));
        
        //then
        assertEquals(1, mock.objectReturningMethodNoArgs());
        assertEquals(2, mock.objectReturningMethodNoArgs());
        assertEquals(3, mock.objectReturningMethodNoArgs());
        //last element is returned continuously
        assertEquals(3, mock.objectReturningMethodNoArgs());
        assertEquals(3, mock.objectReturningMethodNoArgs());
    }
    
    @Test
    public void shouldReturnNullIfNecessary() throws Exception {
        //when
        List<Integer> list = asList(1, null);
        when(mock.objectReturningMethodNoArgs()).thenAnswer(new ReturnsElementsOf(list));
        
        //then
        assertEquals(1, mock.objectReturningMethodNoArgs());
        assertEquals(null, mock.objectReturningMethodNoArgs());
        assertEquals(null, mock.objectReturningMethodNoArgs());
    }
    
    @Test
    public void shouldScreamWhenNullPassed() throws Exception {
        try {
            //when
            new ReturnsElementsOf(null);
            //then
            fail();
        } catch (MockitoException e) {}
    }
}