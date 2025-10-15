/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChecksTest {

    @Test
    public void checkNotNull_not_null() {
        assertEquals("abc", Checks.checkNotNull("abc", "someValue"));
    }

    @Test
    public void checkNotNull_not_null_additional_message() {
        assertEquals("abc", Checks.checkNotNull("abc", "someValue", "Oh no!"));
    }

    @Test
    public void checkNotNull_null() {
        assertThatThrownBy(
                        () -> {
                            Checks.checkNotNull(null, "someValue");
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("someValue should not be null");
    }

    @Test
    public void checkNotNull_null_additonal_message() {
        assertThatThrownBy(
                        () -> {
                            Checks.checkNotNull(null, "someValue", "Oh no!");
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("someValue should not be null. Oh no!");
    }
}
