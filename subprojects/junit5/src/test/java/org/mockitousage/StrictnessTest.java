/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.junit5.ConfiguredWithMockito;
import org.mockito.quality.Strictness;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

@SuppressWarnings("ConstantConditions")
class StrictnessTest {

    @ConfiguredWithMockito(strictness = Strictness.STRICT_STUBS)
    static class StrictStubs {
        @Mock
        private Function<Integer, String> rootMock;

        @Test
        void should_throw_an_exception_on_strict_stubs() {
            Mockito.when(rootMock.apply(10)).thenReturn("Foo");
        }
    }

    @Test
    void session_checks_for_strict_stubs() {
        TestExecutionResult result = invokeTestClassAndRetrieveMethodResult(StrictStubs.class);

        assertThat(result.getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
        assertThat(result.getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
    }

    @ConfiguredWithMockito(strictness = Strictness.STRICT_STUBS)
    static class ConfiguredStrictStubs {
        @Nested
        class NestedStrictStubs {
            @Mock
            private Function<Integer, String> rootMock;

            @Test
            void should_throw_an_exception_on_strict_stubs_in_a_nested_class() {
                Mockito.when(rootMock.apply(10)).thenReturn("Foo");
            }
        }
    }

    @Test
    void session_can_retrieve_strictness_from_parent_class() {
        TestExecutionResult result = invokeTestClassAndRetrieveMethodResult(ConfiguredStrictStubs.class);

        assertThat(result.getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
        assertThat(result.getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
    }

    @ConfiguredWithMockito(strictness = Strictness.STRICT_STUBS)
    static class ParentConfiguredStrictStubs {
        @Nested
        @ConfiguredWithMockito(strictness = Strictness.WARN)
        class ChildConfiguredWarnStubs {
            @Mock
            private Function<Integer, String> rootMock;

            @Test
            void should_throw_an_exception_on_strict_stubs_in_a_nested_class() {
                Mockito.when(rootMock.apply(10)).thenReturn("Foo");
            }
        }
    }

    @Test
    void session_retrieves_closest_strictness_configuration() {
        TestExecutionResult result = invokeTestClassAndRetrieveMethodResult(ParentConfiguredStrictStubs.class);

        assertThat(result.getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
    }

    private TestExecutionResult invokeTestClassAndRetrieveMethodResult(Class<?> clazz) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(
                selectClass(clazz)
            )
            .build();

        Launcher launcher = LauncherFactory.create();

        final TestExecutionResult[] result = new TestExecutionResult[1];

        launcher.registerTestExecutionListeners(new TestExecutionListener() {
            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
                if (testIdentifier.getDisplayName().endsWith("()")) {
                    result[0] = testExecutionResult;
                }
            }

        });

        launcher.execute(request);

        return result[0];
    }
}
