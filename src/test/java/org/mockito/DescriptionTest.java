/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.exceptions.base.MockitoAssertionError;

/**
 * Tests for https://github.com/mockito/mockito/issues/1712
 */
public class DescriptionTest {
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void verify_method_not_called_should_include_description_in_report() {
        final String description = "Failed to call doSomethingElse";
        expectedException.expect(MockitoAssertionError.class);
        expectedException.expectMessage(description);

        Dependency dependency = spy(Dependency.class);
        SystemUnderTest systemUnderTest = new SystemUnderTest();
        systemUnderTest.doNothing(dependency);
        verify(dependency, description(description)).doSomethingElse(false);
    }

    @Test
    public void
            verify_method_called_with_unexpected_argument_should_include_description_in_report() {
        final String description = "Failed to call doSomethingElse with expected argument";
        expectedException.expect(MockitoAssertionError.class);
        expectedException.expectMessage(description);

        Dependency dependency = spy(Dependency.class);
        SystemUnderTest systemUnderTest = new SystemUnderTest();
        systemUnderTest.doSomething(dependency);
        verify(dependency, description(description)).doSomethingElse(false);
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
