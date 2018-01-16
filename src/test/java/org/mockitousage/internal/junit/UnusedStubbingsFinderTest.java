/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.internal.junit;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.junit.UnusedStubbings;
import org.mockito.internal.junit.UnusedStubbingsFinder;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * This unit test lives in 'org.mockitousage' package for a reason.
 * It makes it easy to write tests that depend on the stack trace filtering logic.
 * Long term, it would be nice to have more configurability in TestBase
 *  to make it easy to write such unit tests in 'org.mockito.*' packages.
 */
public class UnusedStubbingsFinderTest extends TestBase {

    UnusedStubbingsFinder finder = new UnusedStubbingsFinder();
    @Mock IMethods mock1;
    @Mock IMethods mock2;

    @Test
    public void no_interactions() throws Exception {
        //expect
        assertEquals(0, finder.getUnusedStubbings((List) asList(mock1, mock2)).size());
        assertEquals(0, finder.getUnusedStubbingsByLocation((List) asList(mock1, mock2)).size());
    }

    @Test
    public void no_stubbings() throws Exception {
        //when
        mock1.simpleMethod();

        //then
        assertEquals(0, finder.getUnusedStubbings((List) asList(mock1, mock2)).size());
        assertEquals(0, finder.getUnusedStubbingsByLocation((List) asList(mock1, mock2)).size());
    }

    @Test
    public void no_unused_stubbings() throws Exception {
        //when
        when(mock1.simpleMethod()).thenReturn("1");
        mock1.simpleMethod();

        //then
        assertEquals(0, finder.getUnusedStubbings((List) asList(mock1, mock2)).size());
        assertEquals(0, finder.getUnusedStubbingsByLocation((List) asList(mock1, mock2)).size());
    }

    @Test
    public void unused_stubbings() throws Exception {
        //when
        when(mock1.simpleMethod()).thenReturn("1");

        //then
        assertEquals(1, finder.getUnusedStubbings((List) asList(mock1, mock2)).size());
        assertEquals(1, finder.getUnusedStubbingsByLocation((List) asList(mock1, mock2)).size());
    }

    @Test
    public void some_unused_stubbings() throws Exception {
        when(mock1.simpleMethod(1)).thenReturn("1");
        when(mock2.simpleMethod(2)).thenReturn("2");
        when(mock2.simpleMethod(3)).thenReturn("3");

        mock2.simpleMethod(2);

        //when
        UnusedStubbings stubbings = finder.getUnusedStubbings((List) asList(mock1, mock2));

        //then
        assertEquals(2, stubbings.size());
        assertEquals("[mock1.simpleMethod(1); stubbed with: [Returns: 1], mock2.simpleMethod(3); stubbed with: [Returns: 3]]",
                stubbings.toString());
    }

    @Test
    public void unused_and_lenient_stubbings() throws Exception {
        when(mock1.simpleMethod(1)).thenReturn("1");
        when(mock1.simpleMethod(2)).thenReturn("2");
        lenient().when(mock2.simpleMethod(3)).thenReturn("3");

        mock1.simpleMethod(1);

        //when
        UnusedStubbings stubbings = finder.getUnusedStubbings((List) asList(mock1, mock2));

        //then
        assertEquals(1, stubbings.size());
        assertEquals("[mock1.simpleMethod(2); stubbed with: [Returns: 2]]",
                stubbings.toString());
    }

    @Test
    public void some_unused_stubbings_by_location() throws Exception {
        when(mock1.simpleMethod(1)).thenReturn("1");
        when(mock2.simpleMethod(2)).thenReturn("2");
        when(mock2.simpleMethod(3)).thenReturn("3");
        lenient().when(mock2.differentMethod()).thenReturn("4"); //will not be included in results

        mock2.simpleMethod(2);

        //when
        Collection stubbings = finder.getUnusedStubbingsByLocation((List) asList(mock1, mock2));

        //then
        assertEquals(2, stubbings.size());
        assertEquals("[mock1.simpleMethod(1);, mock2.simpleMethod(3);]", stubbings.toString());
    }

    @Test
    public void stubbing_used_by_location() throws Exception {
        //when
        //Emulating stubbing in the same location by putting stubbing in the same line:
        when(mock1.simpleMethod(1)).thenReturn("1"); when(mock2.simpleMethod(1)).thenReturn("1");
        //End of emulation
        mock1.simpleMethod(1);

        //then technically unused stubbings exist
        assertEquals(1, finder.getUnusedStubbings((List) asList(mock1, mock2)).size());
        //however if we consider stubbings in the same location as the same stubbing, all is used:
        assertEquals(0, finder.getUnusedStubbingsByLocation((List) asList(mock1, mock2)).size());
    }

    @Test
    public void deduplicates_stubbings_by_location() throws Exception {
        //when
        //Emulating stubbing in the same location by putting stubbing in the same line:
        when(mock1.simpleMethod(1)).thenReturn("1"); when(mock2.simpleMethod(1)).thenReturn("1");
        //End of emulation

        //when
        Collection stubbings = finder.getUnusedStubbingsByLocation((List) asList(mock1, mock2));

        //then
        assertEquals(1, stubbings.size());
    }
}
