/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;

public class SimpleMockitoLoggerTest extends TestBase {

    @Test
    public void shouldLog() throws Exception {
        //given
        SimpleMockitoLogger logger = new SimpleMockitoLogger();
        //when
        logger.log("foo");
        //then
        assertEquals("foo", logger.getLoggedInfo());
    }
}
