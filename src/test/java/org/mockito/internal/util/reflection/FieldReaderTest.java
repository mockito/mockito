/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unused")
public class FieldReaderTest extends TestBase {

    class Foo {
        private final String isNull = null;
        private final String notNull = "";
    }

    @Test
    public void shouldKnowWhenNull() throws Exception {
        //when
        FieldReader reader = new FieldReader(new Foo(), Foo.class.getDeclaredField("isNull"));
        //then
        assertTrue(reader.isNull());
    }

    @Test
    public void shouldKnowWhenNotNull() throws Exception {
        //when
        FieldReader reader = new FieldReader(new Foo(), Foo.class.getDeclaredField("notNull"));
        //then
        assertFalse(reader.isNull());
    }
}
