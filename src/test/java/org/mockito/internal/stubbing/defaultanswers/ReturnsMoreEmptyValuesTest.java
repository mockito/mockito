/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReturnsMoreEmptyValuesTest extends TestBase {

    private ReturnsMoreEmptyValues rv = new ReturnsMoreEmptyValues();

    @Test
    public void shouldReturnEmptyArray() {
        String[] ret = (String[]) rv.returnValueFor((new String[0]).getClass());
        assertTrue(ret.getClass().isArray());
        assertTrue(ret.length == 0);
    }

    @Test
    public void shouldReturnEmptyString() {
        assertEquals("", rv.returnValueFor(String.class));
    }
}
