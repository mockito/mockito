/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import static org.junit.Assert.fail;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

public class WrongSetOfAnnotationsTest extends TestBase {

    @Test(expected = MockitoException.class)
    public void should_not_allow_Mock_and_Spy() throws Exception {
        MockitoAnnotations.openMocks(
                new Object() {
                    @Mock @Spy List<?> mock;
                });
    }

    @Test
    public void should_not_allow_Spy_and_InjectMocks_on_interfaces() throws Exception {
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
    public void should_allow_Spy_and_InjectMocks() throws Exception {
        MockitoAnnotations.openMocks(
                new Object() {
                    @InjectMocks @Spy WithDependency mock;
                });
    }

    static class WithDependency {
        List<?> list;
    }

    @Test(expected = MockitoException.class)
    public void should_not_allow_Mock_and_InjectMocks() throws Exception {
        MockitoAnnotations.openMocks(
                new Object() {
                    @InjectMocks @Mock List<?> mock;
                });
    }

    @Test(expected = MockitoException.class)
    public void should_not_allow_Captor_and_Mock() throws Exception {
        MockitoAnnotations.openMocks(
                new Object() {
                    @Mock @Captor ArgumentCaptor<?> captor;
                });
    }

    @Test(expected = MockitoException.class)
    public void should_not_allow_Captor_and_Spy() throws Exception {
        MockitoAnnotations.openMocks(
                new Object() {
                    @Spy @Captor ArgumentCaptor<?> captor;
                });
    }

    @Test(expected = MockitoException.class)
    public void should_not_allow_Captor_and_InjectMocks() throws Exception {
        MockitoAnnotations.openMocks(
                new Object() {
                    @InjectMocks @Captor ArgumentCaptor<?> captor;
                });
    }
}
