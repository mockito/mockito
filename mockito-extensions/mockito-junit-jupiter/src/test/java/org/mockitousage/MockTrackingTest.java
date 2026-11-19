/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.junit.jupiter.MockTracking;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies the behavior of {@link MockTracking}, in particular
 * {@link MockTracking#ANNOTATED_AND_INSTANTIATED} which extends strictness reporting to mocks
 * that are instantiated manually via {@link org.mockito.Mockito#mock(Class)} and assigned to
 * test fields without an {@link Mock @Mock} annotation.
 */
class MockTrackingTest {

    @ExtendWith(MockitoExtension.class)
    static class DefaultTrackingIgnoresInstantiatedMock {

        private final Function<Integer, String> instantiated = mock();

        @Test
        void over_stubbing_on_instantiated_mock_is_not_reported_by_default() {
            when(instantiated.apply(1)).thenReturn("one");
            // No call on `instantiated` — would be flagged as unused stubbing if tracked.
        }
    }

    @Test
    void by_default_instantiated_mocks_are_not_tracked() {
        TestExecutionResult result =
                invokeTestClassAndRetrieveMethodResult(DefaultTrackingIgnoresInstantiatedMock.class);

        assertSuccessful(result);
    }

    @MockitoSettings(mockTracking = MockTracking.ANNOTATED_AND_INSTANTIATED)
    static class FullTrackingDetectsOverStubbing {

        private final Function<Integer, String> instantiated = mock();

        @Test
        void over_stubbing_on_instantiated_mock_should_be_reported() {
            when(instantiated.apply(1)).thenReturn("one");
        }
    }

    @Test
    void instantiated_mocks_are_tracked_under_annotated_and_instantiated_mode() {
        TestExecutionResult result =
                invokeTestClassAndRetrieveMethodResult(FullTrackingDetectsOverStubbing.class);

        assertStubbingDetected(result);
    }

    @MockitoSettings(mockTracking = MockTracking.ANNOTATED_AND_INSTANTIATED)
    static class FullTrackingStillAllowsUsedStubs {

        private final Function<Integer, String> instantiated = mock();

        @Test
        void used_stubs_should_not_be_reported() {
            when(instantiated.apply(1)).thenReturn("one");
            assertThat(instantiated.apply(1)).isEqualTo("one");
        }

    }
    @Test
    void used_stubbings_on_instantiated_mocks_do_not_fail_under_annotated_and_instantiated() {
        TestExecutionResult result =
                invokeTestClassAndRetrieveMethodResult(FullTrackingStillAllowsUsedStubs.class);

        assertSuccessful(result);
    }

    @MockitoSettings(mockTracking = MockTracking.ANNOTATED_AND_INSTANTIATED)
    static class FullTrackingHandlesAnnotatedMocksWithoutDoubleRegistration {


        @Mock private Function<Integer, String> annotated;

        @Test
        void annotated_mock_over_stubbing_is_still_reported() {
            when(annotated.apply(1)).thenReturn("one");
        }
    }

    @Test
    void annotated_mocks_remain_tracked_exactly_once_under_full_tracking() {
        TestExecutionResult result =
                invokeTestClassAndRetrieveMethodResult(
                        FullTrackingHandlesAnnotatedMocksWithoutDoubleRegistration.class);

        assertStubbingDetected(result);
    }

    abstract static class BaseWithInstantiatedMock {
        final Function<Integer, String> inheritedMock = mock();
    }

    @MockitoSettings(mockTracking = MockTracking.ANNOTATED_AND_INSTANTIATED)
    static class FullTrackingScansSuperclassFields extends BaseWithInstantiatedMock {

        @Test
        void over_stubbing_on_inherited_instantiated_mock_should_be_reported() {
            when(inheritedMock.apply(1)).thenReturn("one");
        }
    }

    @Test
    void instantiated_mocks_in_superclasses_are_tracked() {
        TestExecutionResult result =
                invokeTestClassAndRetrieveMethodResult(FullTrackingScansSuperclassFields.class);

        assertStubbingDetected(result);
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

    private static void assertSuccessful(TestExecutionResult result) {
        assertThat(result.getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
    }

    private static void assertStubbingDetected(TestExecutionResult result) {
        assertThat(result.getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
        assertThat(result.getThrowable())
            .get()
            .isInstanceOf(UnnecessaryStubbingException.class);
    }
}
