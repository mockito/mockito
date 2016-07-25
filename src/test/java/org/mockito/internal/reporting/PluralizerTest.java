/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;

public class PluralizerTest extends TestBase {

    @Test
    public void pluralizes_number() {
        assertEquals("0 times", Pluralizer.pluralize(0));
        assertEquals("1 time", Pluralizer.pluralize(1));
        assertEquals("2 times", Pluralizer.pluralize(2));
        assertEquals("20 times", Pluralizer.pluralize(20));
    }

    @Test
    public void pluralizes_interactions() {
        assertEquals("were exactly 0 interactions", Pluralizer.were_exactly_x_interactions(0));
        assertEquals("was exactly 1 interaction", Pluralizer.were_exactly_x_interactions(1));
        assertEquals("were exactly 100 interactions", Pluralizer.were_exactly_x_interactions(100));
    }
}
