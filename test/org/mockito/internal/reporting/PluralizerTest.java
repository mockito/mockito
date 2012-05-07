/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class PluralizerTest extends TestBase {

    @Test
    public void shouldGetPluralizedNumber() {
        new Pluralizer();
        assertEquals("0 times", Pluralizer.pluralize(0));
        assertEquals("1 time", Pluralizer.pluralize(1));
        assertEquals("2 times", Pluralizer.pluralize(2));
        assertEquals("20 times", Pluralizer.pluralize(20));
    }
}
