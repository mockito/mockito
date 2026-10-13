/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ValuePrintersTest {

    @Test
    public void values_are_passed_to_the_default_renderer_for_printing() {
        assertThat(ValuePrinter.print(null)).isNotNull().isEqualTo("null");
        assertThat(ValuePrinter.print("some string")).isEqualTo("\"some string\"");
        assertThat(ValuePrinter.print(new FormattedText("formatted"))).isEqualTo("formatted");
        assertThat(ValuePrinter.print(new int[]{1, 2})).isEqualTo("[1, 2]");
        assertThat(ValuePrinter.print(Map.of("foo", 2L))).isEqualTo("{\"foo\" = 2L}");
    }

    @Test
    public void printValues_withDefaultSeparator() {
        List<Integer> values = asList(111, 222, 333);
        assertThat(ValuePrinter.printValues(null, null, null, values.iterator())).isEqualTo("(111,222,333)");
    }
}
