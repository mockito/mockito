/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.junit.jupiter.SamsMockitoExtension;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * Test that runs the inner test using a launcher {@see #invokeTestClassAndRetrieveMethodResult}.
 * We then assert on the actual test run output, to see if test actually failed as a result
 * of our extension.
 */
class SamsStrictnessTest {

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
        TestExecutionResult result = invokeTestClassAndRetrieveMethodResult(StrictStubs.class);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
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
        TestExecutionResult result = invokeTestClassAndRetrieveMethodResult(ConfiguredStrictStubs.class);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
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
        TestExecutionResult result = invokeTestClassAndRetrieveMethodResult(ParentConfiguredStrictStubs.class);

        assertThat(result.getStatus()).isEqualTo(Status.SUCCESSFUL);
    }

    @ExtendWith(SamsMockitoExtension.class)
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
        TestExecutionResult result = invokeTestClassAndRetrieveClassResult(ByDefaultUsesStrictStubs.class);

        assertThat(result.getStatus()).withFailMessage(result.toString()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
    }

    @ExtendWith(SamsMockitoExtension.class)
    static class UseStubsAtLeastOnceAcrossTests {

        @Mock
        private Dependency dependency;

        private SystemUnderTest systemUnderTest;

        @BeforeEach
        void setUp() {
            Mockito.when(dependency.doWork()).thenReturn(true);
            systemUnderTest = new SystemUnderTest(dependency);
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

    private interface Dependency {
        boolean doWork();

        boolean workDone();

    }

    @Test
    void use_stubs_at_least_once_across_all_test_cases() {
        TestExecutionResult classResult = invokeTestClassAndRetrieveClassResult(UseStubsAtLeastOnceAcrossTests.class);

        assertThat(classResult.getStatus()).withFailMessage(classResult.toString()).isEqualTo(Status.SUCCESSFUL);
    }


    @ExtendWith(SamsMockitoExtension.class)
    static class UnusedStubInTestsAreStillFail {

        @Mock
        private Dependency dependency;

        private SystemUnderTest systemUnderTest;

        @BeforeEach
        void setUp() {
            Mockito.when(dependency.doWork()).thenReturn(true);
            systemUnderTest = new SystemUnderTest(dependency);
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
        TestExecutionResult classResult = invokeTestClassAndRetrieveClassResult(UnusedStubInTestsAreStillFail.class);

        assertThat(classResult.getStatus()).isEqualTo(Status.FAILED);
        assertThat(classResult.getThrowable().get()).isInstanceOf(UnnecessaryStubbingException.class);
    }

    private TestExecutionResult invokeTestClassAndRetrieveMethodResult(Class<?> clazz) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                                                                          .selectors(selectClass(clazz))
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

    private TestExecutionResult invokeTestClassAndRetrieveClassResult(Class<?> clazz) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                                                                          .selectors(selectClass(clazz))
                                                                          .build();

        Launcher launcher = LauncherFactory.create();

        final TestExecutionResult[] result = new TestExecutionResult[1];

        launcher.registerTestExecutionListeners(new TestExecutionListener() {
            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
                if (testIdentifier.isContainer() && testIdentifier.getParentId().isPresent()) {
                    result[0] = testExecutionResult;
                }
            }

        });

        launcher.execute(request);

        return result[0];
    }
}
