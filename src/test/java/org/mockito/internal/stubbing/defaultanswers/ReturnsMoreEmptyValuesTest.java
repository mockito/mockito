/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;

public class ReturnsMoreEmptyValuesTest extends TestBase {

    private ReturnsMoreEmptyValues rv = new ReturnsMoreEmptyValues();

    @Test
    public void should_return_empty_string() {
        assertEquals("", rv.returnValueFor(String.class));
    }
}
