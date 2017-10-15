/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class AnnotationsTest extends TestBase {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface NotAMock {}

    @Mock List<?> list;
    @Mock final Map<Integer, String> map = new HashMap<Integer, String>();

    @NotAMock Set<?> notAMock;

    @Mock List<?> listTwo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldInitMocks() throws Exception {
        list.clear();
        map.clear();
        listTwo.clear();

        verify(list).clear();
        verify(map).clear();
        verify(listTwo).clear();
    }

    @Test
    public void shouldScreamWhenInitializingMocksForNullClass() throws Exception {
        try {
            MockitoAnnotations.initMocks(null);
            fail();
        } catch (MockitoException e) {
            assertEquals("testClass cannot be null. For info how to use @Mock annotations see examples in javadoc for MockitoAnnotations class",
                    e.getMessage());
        }
    }

    @Test
    public void shouldLookForAnnotatedMocksInSuperClasses() throws Exception {
        Sub sub = new Sub();
        MockitoAnnotations.initMocks(sub);

        assertNotNull(sub.getMock());
        assertNotNull(sub.getBaseMock());
        assertNotNull(sub.getSuperBaseMock());
    }

    @Mock(answer = Answers.RETURNS_MOCKS, name = "i have a name") IMethods namedAndReturningMocks;
    @Mock(answer = Answers.RETURNS_DEFAULTS) IMethods returningDefaults;
    @Mock(extraInterfaces = {List.class}) IMethods hasExtraInterfaces;
    @Mock() IMethods noExtraConfig;
    @Mock(stubOnly=true) IMethods stubOnly;

    @Test
    public void shouldInitMocksWithGivenSettings() throws Exception {
        assertEquals("i have a name", namedAndReturningMocks.toString());
        assertNotNull(namedAndReturningMocks.iMethodsReturningMethod());

        assertEquals("returningDefaults", returningDefaults.toString());
        assertEquals(0, returningDefaults.intReturningMethod());

        assertTrue(hasExtraInterfaces instanceof List);
        assertTrue(Mockito.mockingDetails(stubOnly).getMockCreationSettings().isStubOnly());

        assertEquals(0, noExtraConfig.intReturningMethod());
    }

    class SuperBase {
        @Mock private IMethods mock;

        public IMethods getSuperBaseMock() {
            return mock;
        }
    }

    class Base extends SuperBase {
        @Mock private IMethods mock;

        public IMethods getBaseMock() {
            return mock;
        }
    }

    class Sub extends Base {
        @Mock private IMethods mock;

        public IMethods getMock() {
            return mock;
        }
    }
}
