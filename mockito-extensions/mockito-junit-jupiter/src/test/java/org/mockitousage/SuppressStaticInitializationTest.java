/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.mockito.SuppressStaticInitializationFor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

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
