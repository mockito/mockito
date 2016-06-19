/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.when;

public class StubbingWithExtraAnswersTest extends TestBase {

    @Mock private IMethods mock;
    
    @Test
    public void shouldWorkAsStandardMockito() throws Exception {
        //when
        List<Integer> list = asList(1, 2, 3);
        when(mock.objectReturningMethodNoArgs()).thenAnswer(AdditionalAnswers.returnsElementsOf(list));
        
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
        when(mock.objectReturningMethodNoArgs()).thenAnswer(AdditionalAnswers.returnsElementsOf(list));
        
        //then
        assertEquals(1, mock.objectReturningMethodNoArgs());
        assertEquals(null, mock.objectReturningMethodNoArgs());
        assertEquals(null, mock.objectReturningMethodNoArgs());
    }
    
    @Test
    public void shouldScreamWhenNullPassed() throws Exception {
        try {
            //when
            AdditionalAnswers.returnsElementsOf(null);
            //then
            fail();
        } catch (MockitoException e) {}
    }
}