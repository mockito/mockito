/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

public final class ProgrammaticMockMakerTest {
    @Test
    public void test_normal_mock_uses_given_mock_maker() {
        ClassWithFinalMethod inlineMock =
                Mockito.mock(
                        ClassWithFinalMethod.class, withSettings().mockMaker(MockMakers.INLINE));
        ClassWithFinalMethod subclassMock =
                Mockito.mock(
                        ClassWithFinalMethod.class, withSettings().mockMaker(MockMakers.SUBCLASS));

        Mockito.when(inlineMock.finalMethodCallingNonFinal()).thenReturn("MOCKED");
        Mockito.when(subclassMock.finalMethodCallingNonFinal()).thenReturn("MOCKED");

        assertEquals("MOCKED", inlineMock.finalMethodCallingNonFinal());
        assertEquals("ORIGINAL", subclassMock.finalMethodCallingNonFinal());
        assertEquals("MOCKED", subclassMock.nonFinal());
    }

    @Test
    public void test_mockability_check_uses_given_mock_maker() {
        assertNotNull(Mockito.mock(FinalClass.class, withSettings().mockMaker(MockMakers.INLINE)));
        assertThrows(
                MockitoException.class,
                () ->
                        Mockito.mock(
                                FinalClass.class, withSettings().mockMaker(MockMakers.SUBCLASS)));
    }

    @Test
    public void test_deep_stups_inherit_mock_maker() {
        Container inlineMock =
                Mockito.mock(
                        Container.class,
                        withSettings()
                                .mockMaker(MockMakers.INLINE)
                                .defaultAnswer(Answers.RETURNS_DEEP_STUBS));
        Container subclassMock =
                Mockito.mock(
                        Container.class,
                        withSettings()
                                .mockMaker(MockMakers.SUBCLASS)
                                .defaultAnswer(Answers.RETURNS_DEEP_STUBS));

        assertNotNull(inlineMock.finalClass());
        assertNotNull(inlineMock.subContainer().finalClass());
        assertNull(inlineMock.finalClass().someMethod());
        assertNull(inlineMock.subContainer().finalClass().someMethod());
        assertNull(inlineMock.classWithFinalMethod().finalMethod());
        assertNull(inlineMock.subContainer().classWithFinalMethod().finalMethod());

        assertNull(subclassMock.finalClass());
        assertNull(subclassMock.subContainer().finalClass());
        assertEquals("ORIGINAL", subclassMock.classWithFinalMethod().finalMethod());
        assertEquals("ORIGINAL", subclassMock.subContainer().classWithFinalMethod().finalMethod());
    }

    @Test
    public void test_returned_mocks_inherit_mock_maker() {
        Container inlineMock =
                Mockito.mock(
                        Container.class,
                        withSettings()
                                .mockMaker(MockMakers.INLINE)
                                .defaultAnswer(Answers.RETURNS_MOCKS));
        Container subclassMock =
                Mockito.mock(
                        Container.class,
                        withSettings()
                                .mockMaker(MockMakers.SUBCLASS)
                                .defaultAnswer(Answers.RETURNS_MOCKS));

        assertNotNull(inlineMock.finalClass());
        assertNotNull(inlineMock.subContainer().finalClass());
        assertEquals("", inlineMock.finalClass().someMethod());
        assertEquals("", inlineMock.subContainer().finalClass().someMethod());
        assertEquals("", inlineMock.classWithFinalMethod().finalMethod());
        assertEquals("", inlineMock.subContainer().classWithFinalMethod().finalMethod());

        assertNull(subclassMock.finalClass());
        assertNull(subclassMock.subContainer().finalClass());
        assertEquals("ORIGINAL", subclassMock.classWithFinalMethod().finalMethod());
        assertEquals("ORIGINAL", subclassMock.subContainer().classWithFinalMethod().finalMethod());
    }

    @Test
    public void test_smart_nulls_inherit_mock_maker() {
        Container inlineMock =
                Mockito.mock(
                        Container.class,
                        withSettings()
                                .mockMaker(MockMakers.INLINE)
                                .defaultAnswer(Answers.RETURNS_SMART_NULLS));
        Container subclassMock =
                Mockito.mock(
                        Container.class,
                        withSettings()
                                .mockMaker(MockMakers.SUBCLASS)
                                .defaultAnswer(Answers.RETURNS_SMART_NULLS));

        assertNotNull(inlineMock.finalClass());
        assertNotNull(inlineMock.classWithFinalMethod());
        assertThrows(SmartNullPointerException.class, () -> inlineMock.finalClass().someMethod());
        assertThrows(
                SmartNullPointerException.class,
                () -> inlineMock.classWithFinalMethod().finalMethod());

        assertNull(subclassMock.finalClass());
        assertNotNull(subclassMock.classWithFinalMethod());
        assertEquals("ORIGINAL", subclassMock.classWithFinalMethod().finalMethod());
    }

    @Test
    public void test_custom_mock_maker() {
        assertThatThrownBy(
                        () -> {
                            Mockito.mock(
                                    Container.class,
                                    withSettings().mockMaker(CustomMockMaker.class.getName()));
                        })
                .hasMessage("CUSTOM MOCK MAKER");
    }

    @Test
    public void test_exception_when_mock_maker_cannot_be_instantiated() {
        class InvalidMockMaker extends SubclassByteBuddyMockMaker {
            // Local classes have an implicit constructor parameter,
            // which makes them an invalid mock maker.
        }
        assertThatThrownBy(
                        () -> {
                            Mockito.mock(
                                    Container.class,
                                    withSettings().mockMaker(InvalidMockMaker.class.getName()));
                        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Failed to construct MockMaker")
                .hasMessageContaining(InvalidMockMaker.class.getName());
    }

    private static final class FinalClass {
        String someMethod() {
            return "ORIGINAL";
        }
    }

    private static class ClassWithFinalMethod {
        final String finalMethod() {
            return "ORIGINAL";
        }

        final String finalMethodCallingNonFinal() {
            nonFinal();
            return "ORIGINAL";
        }

        String nonFinal() {
            return "ORIGINAL";
        }
    }

    private static class Container {
        FinalClass finalClass() {
            return new FinalClass();
        }

        ClassWithFinalMethod classWithFinalMethod() {
            return new ClassWithFinalMethod();
        }

        Container subContainer() {
            return new Container();
        }
    }

    public static class CustomMockMaker extends SubclassByteBuddyMockMaker {
        @Override
        public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
            throw new RuntimeException("CUSTOM MOCK MAKER");
        }
    }
}
