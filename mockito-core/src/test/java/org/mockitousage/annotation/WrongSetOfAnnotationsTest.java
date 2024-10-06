/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

public class WrongSetOfAnnotationsTest extends TestBase {

    @Test
    public void should_not_allow_Mock_and_Spy() {
        assertThatThrownBy(
                        () -> {
                            MockitoAnnotations.openMocks(
                                    new Object() {
                                        @Mock @Spy List<?> mock;
                                    });
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessage(
                        "This combination of annotations is not permitted on a single field:\n"
                                + "@Spy and @Mock");
    }

    @Test
    public void should_not_allow_Spy_and_InjectMocks_on_interfaces() {
        try {
            MockitoAnnotations.openMocks(
                    new Object() {
                        @InjectMocks @Spy List<?> mock;
                    });
            fail();
        } catch (MockitoException me) {
            Assertions.assertThat(me.getMessage()).contains("'List' is an interface");
        }
    }

    @Test
    public void should_allow_Spy_and_InjectMocks() {
        MockitoAnnotations.openMocks(
                new Object() {
                    @InjectMocks @Spy WithDependency mock;
                });
    }

    static class WithDependency {
        List<?> list;
    }

    @Test
    public void should_not_allow_Mock_and_InjectMocks() {
        assertThatThrownBy(
                        () -> {
                            MockitoAnnotations.openMocks(
                                    new Object() {
                                        @InjectMocks @Mock List<?> mock;
                                    });
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessage(
                        "This combination of annotations is not permitted on a single field:\n"
                                + "@Mock and @InjectMocks");
    }

    @Test
    public void should_not_allow_Captor_and_Mock() {
        assertThatThrownBy(
                        () -> {
                            MockitoAnnotations.openMocks(
                                    new Object() {
                                        @Mock @Captor ArgumentCaptor<?> captor;
                                    });
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "You cannot have more than one Mockito annotation on a field!",
                        "The field 'captor' has multiple Mockito annotations.",
                        "For info how to use annotations see examples in javadoc for MockitoAnnotations class.");
    }

    @Test
    public void should_not_allow_Captor_and_Spy() {
        assertThatThrownBy(
                        () -> {
                            MockitoAnnotations.openMocks(
                                    new Object() {
                                        @Spy @Captor ArgumentCaptor<?> captor;
                                    });
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessage(
                        "This combination of annotations is not permitted on a single field:\n"
                                + "@Spy and @Captor");
    }

    @Test
    public void should_not_allow_Captor_and_InjectMocks() {
        assertThatThrownBy(
                        () -> {
                            MockitoAnnotations.openMocks(
                                    new Object() {
                                        @InjectMocks @Captor ArgumentCaptor<?> captor;
                                    });
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessage(
                        "This combination of annotations is not permitted on a single field:\n"
                                + "@Captor and @InjectMocks");
    }
}
