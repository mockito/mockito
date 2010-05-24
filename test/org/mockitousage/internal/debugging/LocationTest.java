/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.internal.debugging;

import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.base.StackTraceFilter;
import org.mockitoutil.TestBase;

@SuppressWarnings("serial")
public class LocationTest extends TestBase {

    @Test
    public void shouldLocationNotContainGetStackTraceMethod() {
        assertContains("shouldLocationNotContainGetStackTraceMethod", new Location().toString());
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
        String loc = new Location(filterReturningEmptyArray).toString();

        //then
        assertEquals("-> at <<unknown line>>", loc);
    }
}