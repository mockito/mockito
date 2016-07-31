/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.internal.debugging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;

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
}