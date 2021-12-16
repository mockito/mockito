/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;

/**
 * Tests for https://github.com/mockito/mockito/issues/1712
 */
public class DescriptionTest {

    @Test
    public void verify_method_not_called_should_include_description_in_report() {
        final String description = "Failed to call doSomethingElse";

        Dependency dependency = spy(Dependency.class);
        SystemUnderTest systemUnderTest = new SystemUnderTest();
        systemUnderTest.doNothing(dependency);

        assertThatThrownBy(
                        () -> {
                            verify(dependency, description(description)).doSomethingElse(false);
                        })
                .isInstanceOf(MockitoAssertionError.class)
                .hasMessageContaining(description);
    }

    @Test
    public void
            verify_method_called_with_unexpected_argument_should_include_description_in_report() {
        final String description = "Failed to call doSomethingElse with expected argument";

        Dependency dependency = spy(Dependency.class);
        SystemUnderTest systemUnderTest = new SystemUnderTest();
        systemUnderTest.doSomething(dependency);

        assertThatThrownBy(
                        () -> {
                            verify(dependency, description(description)).doSomethingElse(false);
                        })
                .isInstanceOf(MockitoAssertionError.class)
                .hasMessageContaining(description);
    }

    static class SystemUnderTest {
        @SuppressWarnings("unused")
        void doNothing(Dependency dependency) {}

        void doSomething(Dependency dependency) {
            dependency.doSomethingElse(true);
        }
    }

    static class Dependency {
        @SuppressWarnings("unused")
        void doSomethingElse(boolean value) {}
    }
}
