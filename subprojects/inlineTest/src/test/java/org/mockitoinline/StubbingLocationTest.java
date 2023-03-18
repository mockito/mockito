/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import java.util.Collections;
import java.util.Set;
import org.junit.Test;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.stubbing.Stubbing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StubbingLocationTest {

    @Test
    public void stubbing_location_should_be_the_correct_point() {
        ConcreteClass mock = mock(ConcreteClass.class);
        String frame;
        // Initializing 'frame' at the method parameter point is to gain the exact line number of the stubbing point.
        when(mock.concreteMethod(frame = Thread.currentThread().getStackTrace()[1].toString())).thenReturn("");
        mock.concreteMethod(frame);
        Set<Stubbing> stubbings = AllInvocationsFinder.findStubbings(Collections.singleton(mock));
        assertEquals(1, stubbings.size());
        String location = stubbings.iterator().next().getInvocation().getLocation().toString();
        assertEquals("-> at " + frame, location);
    }

    static final class ConcreteClass {
        String concreteMethod(String s) {
            throw new RuntimeException(s);
        }
    }

}
