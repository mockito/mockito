/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.internal.debugging;

import org.junit.Test;
import org.mockito.internal.debugging.LocationFactory;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class LocationFactoryTest extends TestBase {

    @Test
    public void shouldLocationNotContainGetStackTraceMethod() {
        assertThat(LocationFactory.create().toString())
                .contains("shouldLocationNotContainGetStackTraceMethod");
    }

    @Test
    public void provides_location_class() {
        // when
        final List<String> files = new ArrayList<String>();
        new Runnable() { // anonymous inner class adds stress to the check
            public void run() {
                files.add(LocationFactory.create().getSourceFile());
            }
        }.run();

        // then
        assertEquals("LocationFactoryTest.java", files.get(0));
    }
}
