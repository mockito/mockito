/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.Mockito.*;

//see issue 184
public class ShouldMocksCompareToBeConsistentWithEqualsTest extends TestBase {

    @Test
    public void shouldCompareToBeConsistentWithEquals() {
        //given
        Date today    = mock(Date.class);
        Date tomorrow = mock(Date.class);

        //when
        Set<Date> set = new TreeSet<Date>();
        set.add(today);
        set.add(tomorrow);

        //then
        assertEquals(2, set.size());
    }

    @Test
    public void shouldAllowStubbingAndVerifyingCompareTo() {
        //given
        Date mock    = mock(Date.class);
        when(mock.compareTo(any(Date.class))).thenReturn(10);

        //when
        mock.compareTo(new Date());

        //then
        assertEquals(10, mock.compareTo(new Date()));
        verify(mock, atLeastOnce()).compareTo(any(Date.class));
    }

    @Test
    public void shouldResetNotRemoveDefaultStubbing() {
        //given
        Date mock    = mock(Date.class);
        reset(mock);

        //then
        assertEquals(1, mock.compareTo(new Date()));
    }
}