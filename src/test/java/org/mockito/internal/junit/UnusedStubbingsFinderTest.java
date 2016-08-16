package org.mockito.internal.junit;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UnusedStubbingsFinderTest extends TestBase {

    UnusedStubbingsFinder finder = new UnusedStubbingsFinder();
    @Mock IMethods mock1;
    @Mock IMethods mock2;

    @Test
    public void no_interactions() throws Exception {
        //when
        UnusedStubbings stubbings = finder.getUnusedStubbings((List) asList(mock1, mock2));

        //then
        assertEquals(0, stubbings.size());
    }

    @Test
    public void no_stubbings() throws Exception {
        mock1.simpleMethod();

        //when
        UnusedStubbings stubbings = finder.getUnusedStubbings((List) asList(mock1, mock2));

        //then
        assertEquals(0, stubbings.size());
    }

    @Test
    public void no_unused_stubbings() throws Exception {
        when(mock1.simpleMethod()).thenReturn("1");
        mock1.simpleMethod();

        //when
        UnusedStubbings stubbings = finder.getUnusedStubbings((List) asList(mock1, mock2));

        //then
        assertEquals(0, stubbings.size());
    }

    @Test
    public void unused_stubbings() throws Exception {
        when(mock1.simpleMethod()).thenReturn("1");

        //when
        UnusedStubbings stubbings = finder.getUnusedStubbings((List) asList(mock1, mock2));

        //then
        assertEquals(1, stubbings.size());
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
}