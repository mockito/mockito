/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class MoreEmptyReturnValuesTest extends TestBase {

    private MoreEmptyReturnValues rv = new MoreEmptyReturnValues();

    @Test
    public void shouldReturnEmptyArray() {
        String[] ret = (String[]) rv.returnValueFor((new String[0]).getClass());
        ret.getClass().isArray();
        assertTrue(ret.length == 0);
    }
    
    @Test
    public void shouldReturnEmptyString() {
        assertEquals("", rv.returnValueFor(String.class));
    }
}