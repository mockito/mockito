/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockitoutil.ExceptionHider;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockitoutil.ExceptionHider.wrap;

public class TerminalMockCandidateFilterTest {

    private static class Content {
        public String name;

        public Content(String name) {
            this.name = name;
        }
    }

    private static class TargetClass {
        @SuppressWarnings("FieldMayBeFinal")
        private Content content = null;
    }

    private static class StaticFinalClass {
        private static final Content content = null;
    }

    private final TerminalMockCandidateFilter filter = new TerminalMockCandidateFilter();
    private final TargetClass targetInstance = new TargetClass();
    private final Field targetField = wrap(() -> TargetClass.class.getDeclaredField("content"));
    private final Content mock = new Content("candidate");

    @Test
    public void injects_mock_into_field() {
        filter.filterCandidate(
                        Collections.singletonList(mock),
                        targetField,
                        Collections.emptyList(),
                        targetInstance)
                .thenInject();

        assertThat(targetInstance.content).isSameAs(mock);
    }

    @Test
    public void does_nothing_if_no_mocks_present() {
        filter.filterCandidate(
                        Collections.emptyList(),
                        targetField,
                        Collections.emptyList(),
                        targetInstance)
                .thenInject();

        assertThat(targetInstance.content).isNull();
    }

    @Test
    public void does_nothing_if_more_than_one_mock() {
        filter.filterCandidate(
                        Arrays.asList(mock, new Content("other mock")),
                        targetField,
                        Collections.emptyList(),
                        targetInstance)
                .thenInject();

        assertThat(targetInstance.content).isNull();
    }

    @Test
    public void injects_mock_into_static_final_field() {
        StaticFinalClass targetInstance = new StaticFinalClass();
        Field targetField =
                ExceptionHider.wrap(() -> StaticFinalClass.class.getDeclaredField("content"));

        filter.filterCandidate(
                        Collections.singletonList(mock),
                        targetField,
                        Collections.emptyList(),
                        targetInstance)
                .thenInject();

        assertThat(StaticFinalClass.content).isSameAs(mock);
    }
}
