/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockitoutil.TestBase;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class SameTest extends TestBase {

    @Test
    public void shouldInferType() {
        assertThat(new Same("String").type()).isEqualTo(String.class);
    }

    @Test
    public void shouldDefaultTypeOnNull() {
        assertThat(new Same(null).type())
                .isEqualTo(((ArgumentMatcher<Object>) argument -> false).type());
    }
}
