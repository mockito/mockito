/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.mockito.MockedStatic;
import org.mockito.SuppressStaticInitializationFor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.plugins.MockMaker;

/**
 * Tests the {@link SuppressStaticInitializationFor} annotation integration with {@link
 * MockitoExtension}. Since these tests run without {@code -javaagent} (premain), the suppression
 * will fail at the MockMaker level. These tests verify the annotation processing and error handling
 * paths.
 *
 * <p>Inner test classes are invoked only via the JUnit {@link Launcher}, not directly by the test
 * engine. They use package-private visibility to prevent independent discovery.
 */
class SuppressStaticInitializationTest {

    @ExtendWith(MockitoExtension.class)
    @SuppressStaticInitializationFor({"org.mockitousage.SomeNonExistentClass"})
    static class SuppressWithoutPremain {

        @Test
        void should_fail_because_premain_is_not_attached() {
            // This test should not reach here because beforeAll should fail
        }
    }

    @Test
    void annotation_fails_without_premain_agent() {
        TestExecutionResult result =
                invokeTestClassAndRetrieveContainerResult(SuppressWithoutPremain.class);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
        assertThat(result.getThrowable()).isPresent();
        assertThat(result.getThrowable().get()).isInstanceOf(MockitoException.class);
        assertThat(result.getThrowable().get().getMessage()).contains("javaagent");
    }

    @ExtendWith(MockitoExtension.class)
    @SuppressStaticInitializationFor({})
    static class SuppressWithEmptyArray {

        @Test
        void should_succeed_with_empty_suppression_list() {
            assertThat(true).isTrue();
        }
    }

    @Test
    void annotation_with_empty_array_does_not_fail() {
        TestExecutionResult result =
                invokeTestClassAndRetrieveMethodResult(SuppressWithEmptyArray.class);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
    }

    @ExtendWith(MockitoExtension.class)
    static class NoSuppressAnnotation {

        @Test
        void should_succeed_without_annotation() {
            assertThat(true).isTrue();
        }
    }

    @Test
    void no_annotation_does_not_fail() {
        TestExecutionResult result =
                invokeTestClassAndRetrieveMethodResult(NoSuppressAnnotation.class);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
    }

    @Test
    void annotation_is_retained_at_runtime() {
        SuppressStaticInitializationFor annotation =
                SuppressWithoutPremain.class.getAnnotation(SuppressStaticInitializationFor.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).containsExactly("org.mockitousage.SomeNonExistentClass");
    }

    @SuppressStaticInitializationFor({"some.Class"})
    static class ParentWithAnnotation {}

    static class ChildWithoutAnnotation extends ParentWithAnnotation {}

    @Test
    void annotation_is_inherited() {
        SuppressStaticInitializationFor annotation =
                ChildWithoutAnnotation.class.getAnnotation(SuppressStaticInitializationFor.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).containsExactly("some.Class");
    }

    @Test
    void beforeAll_stores_and_afterAll_restores_suppressed_classes() throws Exception {
        // Create all mocks BEFORE mocking Plugins to avoid circular dependency
        ExtensionContext context = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        MockMaker mockMaker = mock(MockMaker.class);

        List<String> classNames = Arrays.asList("org.mockitousage.SomeNonExistentClass");

        when(context.getRequiredTestClass()).thenReturn((Class) SuppressWithoutPremain.class);
        when(context.getStore(any())).thenReturn(store);
        // afterAll calls store.remove which should return what beforeAll stored
        when(store.remove("suppressedClasses")).thenReturn(classNames);

        MockitoExtension extension = new MockitoExtension();

        try (MockedStatic<Plugins> plugins = mockStatic(Plugins.class)) {
            plugins.when(Plugins::getMockMaker).thenReturn(mockMaker);

            extension.beforeAll(context);

            verify(mockMaker).suppressStaticInitializationFor(classNames);
            verify(store).put("suppressedClasses", classNames);

            extension.afterAll(context);

            verify(mockMaker).restoreStaticInitializationFor(classNames);
        }
    }

    private TestExecutionResult invokeTestClassAndRetrieveMethodResult(Class<?> clazz) {
        LauncherDiscoveryRequest request =
                LauncherDiscoveryRequestBuilder.request().selectors(selectClass(clazz)).build();

        Launcher launcher = LauncherFactory.create();

        final TestExecutionResult[] result = new TestExecutionResult[1];

        launcher.registerTestExecutionListeners(
                new TestExecutionListener() {
                    @Override
                    public void executionFinished(
                            TestIdentifier testIdentifier,
                            TestExecutionResult testExecutionResult) {
                        if (testIdentifier.getDisplayName().endsWith("()")) {
                            result[0] = testExecutionResult;
                        }
                    }
                });

        launcher.execute(request);

        return result[0];
    }

    /**
     * Retrieves the container-level result (from beforeAll/afterAll), not the individual test
     * method result. This is needed because {@code beforeAll} failures are reported on the
     * container, not on individual tests.
     */
    private TestExecutionResult invokeTestClassAndRetrieveContainerResult(Class<?> clazz) {
        LauncherDiscoveryRequest request =
                LauncherDiscoveryRequestBuilder.request().selectors(selectClass(clazz)).build();

        Launcher launcher = LauncherFactory.create();

        final TestExecutionResult[] result = new TestExecutionResult[1];

        launcher.registerTestExecutionListeners(
                new TestExecutionListener() {
                    @Override
                    public void executionFinished(
                            TestIdentifier testIdentifier,
                            TestExecutionResult testExecutionResult) {
                        if (testIdentifier.isTest()) {
                            // Skip individual test results
                            return;
                        }
                        if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
                            result[0] = testExecutionResult;
                        }
                    }
                });

        launcher.execute(request);

        return result[0];
    }
}
