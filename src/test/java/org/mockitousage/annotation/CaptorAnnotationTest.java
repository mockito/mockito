/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CaptorAnnotationTest extends TestBase {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface NotAMock {
    }

    @Captor
    final ArgumentCaptor<String> finalCaptor = ArgumentCaptor.forClass(String.class);

    @Captor
    ArgumentCaptor<List<List<String>>> genericsCaptor;

    @SuppressWarnings("rawtypes")
    @Captor
    ArgumentCaptor nonGenericCaptorIsAllowed;

    @Mock
    MockInterface mockInterface;

    @NotAMock
    Set<?> notAMock;

    public interface MockInterface {
        void testMe(String simple, List<List<String>> genericList);
    }

    @Test
    public void testNormalUsage() {

        MockitoAnnotations.initMocks(this);

        // check if assigned correctly
        assertNotNull(finalCaptor);
        assertNotNull(genericsCaptor);
        assertNotNull(nonGenericCaptorIsAllowed);
        assertNull(notAMock);

        // use captors in the field to be sure they are cool
        String argForFinalCaptor = "Hello";
        ArrayList<List<String>> argForGenericsCaptor = new ArrayList<List<String>>();

        mockInterface.testMe(argForFinalCaptor, argForGenericsCaptor);

        Mockito.verify(mockInterface).testMe(finalCaptor.capture(), genericsCaptor.capture());

        assertEquals(argForFinalCaptor, finalCaptor.getValue());
        assertEquals(argForGenericsCaptor, genericsCaptor.getValue());

    }

    public static class WrongType {
        @Captor
        List<?> wrongType;
    }

    @Test
    public void shouldScreamWhenWrongTypeForCaptor() {
        try {
            MockitoAnnotations.initMocks(new WrongType());
            fail();
        } catch (MockitoException e) {}
    }

    public static class ToManyAnnotations {
        @Captor
        @Mock
        ArgumentCaptor<List> missingGenericsField;
    }

    @Test
    public void shouldScreamWhenMoreThanOneMockitoAnnotaton() {
        try {
            MockitoAnnotations.initMocks(new ToManyAnnotations());
            fail();
        } catch (MockitoException e) {
            assertThat(e)
                .hasMessageContaining("missingGenericsField")
                .hasMessageContaining("multiple Mockito annotations");
        }
    }

    @Test
    public void shouldScreamWhenInitializingCaptorsForNullClass() throws Exception {
        try {
            MockitoAnnotations.initMocks(null);
            fail();
        } catch (MockitoException e) {
        }
    }

    @Test
    public void shouldLookForAnnotatedCaptorsInSuperClasses() throws Exception {
        Sub sub = new Sub();
        MockitoAnnotations.initMocks(sub);

        assertNotNull(sub.getCaptor());
        assertNotNull(sub.getBaseCaptor());
        assertNotNull(sub.getSuperBaseCaptor());
    }

    class SuperBase {
        @Captor
        private ArgumentCaptor<IMethods> mock;

        public ArgumentCaptor<IMethods> getSuperBaseCaptor() {
            return mock;
        }
    }

    class Base extends SuperBase {
        @Captor
        private ArgumentCaptor<IMethods> mock;

        public ArgumentCaptor<IMethods> getBaseCaptor() {
            return mock;
        }
    }

    class Sub extends Base {
        @Captor
        private ArgumentCaptor<IMethods> mock;

        public ArgumentCaptor<IMethods> getCaptor() {
            return mock;
        }
    }
}