/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
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
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * Test that runs the inner test using a launcher {@see #invokeTestClassAndRetrieveMethodResult}.
 * We then assert on the actual test run output, to see if test actually failed as a result
 * of our extension.
 */
@SuppressWarnings("ConstantConditions")
class StrictnessTest {

    @MockitoSettings(strictness = Strictness.STRICT_STUBS)
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
        Map<String, TestExecutionResult> result = invokeTestClassAndRetrieveMethodResults(StrictStubs.class);
        assertThat(result.get("StrictnessTest$StrictStubs").getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
        assertThat(result.get("StrictnessTest$StrictStubs").getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
        //TODO should fail at test level
//        assertThat(result.get("should_throw_an_exception_on_strict_stubs()").getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
//        assertThat(result.get("should_throw_an_exception_on_strict_stubs()").getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
    }

    @MockitoSettings(strictness = Strictness.STRICT_STUBS)
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
        Map<String, TestExecutionResult> result = invokeTestClassAndRetrieveMethodResults(ConfiguredStrictStubs.class);

        assertThat(result.get("StrictnessTest$ConfiguredStrictStubs").getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
        assertThat(result.get("StrictnessTest$ConfiguredStrictStubs").getThrowable().orElse(null)).isNull();
        assertThat(result.get("NestedStrictStubs").getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
        assertThat(result.get("NestedStrictStubs").getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
//        assertThat(result.get("should_throw_an_exception_on_strict_stubs_in_a_nested_class()").getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
//        assertThat(result.get("should_throw_an_exception_on_strict_stubs_in_a_nested_class()").getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
    }

    @MockitoSettings(strictness = Strictness.STRICT_STUBS)
    static class ParentConfiguredStrictStubs {
        @Nested
        @MockitoSettings(strictness = Strictness.WARN)
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
        Map<String, TestExecutionResult> result = invokeTestClassAndRetrieveMethodResults(ParentConfiguredStrictStubs.class);
        for (Map.Entry<String, TestExecutionResult> testResult : result.entrySet()) {
            assertThat(testResult.getValue().getStatus()).withFailMessage(testResult.getKey() + " should have been SUCCESSFUL but was " + testResult.getValue()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
        }
    }

    @ExtendWith(MockitoExtension.class)
    static class ByDefaultUsesStrictStubs {

        @Mock
        private Function<Integer, String> rootMock;

        @Test
        void should_throw_an_exception_on_strict_stubs_configured_by_default() {
            Mockito.when(rootMock.apply(10)).thenReturn("Foo");
        }

    }

    @Test
    void by_default_configures_strict_stubs_in_runner() {
        Map<String, TestExecutionResult> result = invokeTestClassAndRetrieveMethodResults(ByDefaultUsesStrictStubs.class);

        assertThat(result.get("StrictnessTest$ByDefaultUsesStrictStubs").getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
        assertThat(result.get("StrictnessTest$ByDefaultUsesStrictStubs").getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
    }

    @ExtendWith(MockitoExtension.class)
    static class UnusedStubInTestsStillFail {

        @Mock
        private Dependency dependency;

        private SystemUnderTest systemUnderTest;

        @BeforeEach
        void setUp() {
            systemUnderTest = new SystemUnderTest(dependency);
        }

        @Test
        void shouldDoWork() {
            //Given
            Mockito.when(dependency.doWork()).thenReturn(true);

            //When
            systemUnderTest.doThing(true);

            //Then
            Mockito.verify(dependency).doWork();
        }

        @Test
        void shouldDoNoWork() {
            //Given
            Mockito.when(dependency.doWork()).thenReturn(true);

            //When
            systemUnderTest.doThing(false);

            //Then
            Mockito.verifyZeroInteractions(dependency);
        }


        private class SystemUnderTest {

            private final Dependency dependency;


            private SystemUnderTest(final Dependency dependency) {
                this.dependency = dependency;
            }


            boolean doThing(boolean works) {
                if (works) {
                    return dependency.doWork();
                } else {
                    return false;
                }
            }
        }

    }

    @Test
    void unnessesary_stubbing_in_test_still_caught() {
        final Map<String, TestExecutionResult> resultsMap = invokeTestClassAndRetrieveMethodResults(UnusedStubInTestsStillFail.class);

        assertThat(resultsMap.get("shouldDoWork()").getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);

        assertThat(resultsMap.get("StrictnessTest$UnusedStubInTestsStillFail").getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
        assertThat(resultsMap.get("StrictnessTest$UnusedStubInTestsStillFail").getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
        //TODO actually fail `shouldDoNoWork()` instead of the class
//        assertThat(resultsMap.get("shouldDoNoWork()").getStatus()).isEqualTo(TestExecutionResult.Status.FAILED);
//        assertThat(resultsMap.get("shouldDoNoWork()").getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
    }

    @ExtendWith(MockitoExtension.class)
    static class UseStubsAtLeastOnceAcrossTests {

        @Mock
        private Dependency dependency;

        private UseStubsAtLeastOnceAcrossTests.SystemUnderTest systemUnderTest;

        @BeforeEach
        void setUp() {
            Mockito.when(dependency.doWork()).thenReturn(true);
            systemUnderTest = new UseStubsAtLeastOnceAcrossTests.SystemUnderTest(dependency);
        }

        @Test
        void shouldDoWork() {
            //Given

            //When
            systemUnderTest.doThing(true);

            //Then
            Mockito.verify(dependency).doWork();
        }

        @Test
        void shouldDoNoWork() {
            //Given

            //When
            systemUnderTest.doThing(false);

            //Then
            Mockito.verifyZeroInteractions(dependency);
        }


        private class SystemUnderTest {

            private final Dependency dependency;


            private SystemUnderTest(final Dependency dependency) {
                this.dependency = dependency;
            }


            boolean doThing(boolean works) {
                if (works) {
                    return dependency.doWork();
                } else {
                    return false;
                }
            }
        }

    }

    @Test
    void use_stubs_at_least_once_across_all_test_cases() {
        final Map<String, TestExecutionResult> resultsMap = invokeTestClassAndRetrieveMethodResults(UseStubsAtLeastOnceAcrossTests.class);

        assertThat(resultsMap.get("shouldDoWork()").getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
        assertThat(resultsMap.get("shouldDoNoWork()").getStatus()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL);
    }

    private interface Dependency {
        boolean doWork();

        boolean workDone();

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
                if (testIdentifier.getType().isTest()) {
                    result[0] = testExecutionResult;
                }
            }

        });

        launcher.execute(request);

        return result[0];
    }

    private Map<String, TestExecutionResult> invokeTestClassAndRetrieveMethodResults(Class<?> clazz) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                                                                          .selectors(selectClass(clazz))
                                                                          .build();

        Launcher launcher = LauncherFactory.create();

        final Map<String, TestExecutionResult> results = new HashMap<>();

        launcher.registerTestExecutionListeners(new TestExecutionListener() {
            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
//                if (testIdentifier.isTest()) {
                    results.put(testIdentifier.getDisplayName(), testExecutionResult);
//                }
            }
        });

        launcher.execute(request);

        return results;
    }
}
