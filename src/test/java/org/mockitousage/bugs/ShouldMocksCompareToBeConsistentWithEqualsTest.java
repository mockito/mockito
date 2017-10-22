/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Test;
import org.mockitoutil.TestBase;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

//see issue 184
public class ShouldMocksCompareToBeConsistentWithEqualsTest extends TestBase {

    @Test
    public void should_compare_to_be_consistent_with_equals() {
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
    public void should_compare_to_be_consistent_with_equals_when_comparing_the_same_reference() {
        //given
        Date today    = mock(Date.class);

        //when
        Set<Date> set = new TreeSet<Date>();
        set.add(today);
        set.add(today);

        //then
        assertEquals(1, set.size());
    }

    @Test
    public void should_allow_stubbing_and_verifying_compare_to() {
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
    public void should_reset_not_remove_default_stubbing() {
        //given
        Date mock    = mock(Date.class);
        reset(mock);

        //then
        assertEquals(1, mock.compareTo(new Date()));
    }
}
