/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.internal.debugging;

import org.junit.Test;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("serial")
public class LocationImplTest extends TestBase {

    @Test
    public void shouldLocationNotContainGetStackTraceMethod() {
        assertThat(new LocationImpl().toString()).contains("shouldLocationNotContainGetStackTraceMethod");
    }

    @Test
    public void shouldBeSafeInCaseForSomeReasonFilteredStackTraceIsEmpty() {
        //given
        StackTraceFilter filterReturningEmptyArray = new StackTraceFilter() {
            @Override
            public StackTraceElement[] filter(StackTraceElement[] target, boolean keepTop) {
                return new StackTraceElement[0];
            }
        };

        //when
        String loc = new LocationImpl(filterReturningEmptyArray).toString();

        //then
        assertEquals("-> at <<unknown line>>", loc);
    }

    @Test
    public void provides_location_class() {
        //when
        final List<String> files = new ArrayList<String>();
        new Runnable() { //anonymous inner class adds stress to the check
            public void run() {
                files.add(new LocationImpl().getSourceFile());
            }
        }.run();

        //then
        assertEquals("LocationImplTest.java", files.get(0));
    }
}
