/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonEqualsTest {

    @Test
    public void shouldBeEqual() {
        assertEquals(new JsonEquals("{}"), new JsonEquals("{}"));
        assertTrue(new JsonEquals("{}").matches("{}"));

        assertEquals(new JsonEquals("{\"firstName\":\"Bill\", \"lastName\":\"Gates\"}"), new JsonEquals("{\"lastName\":\"Gates\", \"firstName\":\"Bill\"}"));
        assertTrue(new JsonEquals("{\"firstName\":\"Bill\", \"lastName\":\"Gates\"}").matches("{\"lastName\":\"Gates\", \"firstName\":\"Bill\"}"));

        assertEquals(new JsonEquals("[]"), new JsonEquals("[]"));
        assertTrue(new JsonEquals("[]").matches("[]"));
    }

}
