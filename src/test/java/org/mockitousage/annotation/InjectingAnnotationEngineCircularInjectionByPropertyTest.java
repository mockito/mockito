/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InjectingAnnotationEngineCircularInjectionByPropertyTest {

    public static class SpiedService1 {
        SpiedService2 spiedService2;
        void assertInjection() {
            assertNotNull(spiedService2);
        }
    }

    public static class SpiedService2 {
        SpiedService1 spiedService1;
        void assertInjection() {
            assertNotNull(spiedService1);
        }
    }

    public static class TestedService {
        SpiedService1 spiedService1;
        SpiedService2 spiedService2;
        void assertInjection() {
            assertNotNull(spiedService1);
            assertNotNull(spiedService2);
            spiedService1.assertInjection();
            spiedService2.assertInjection();
        }
    }

    @InjectMocks
    private TestedService testedService;

    @InjectMocks
    @Spy
    private SpiedService1 spiedService1;

    @InjectMocks
    @Spy
    private SpiedService2 spiedService2;

    @Test
    public void should_inject_mocks_and_spies_by_property() {
        new InjectingAnnotationEngine().process(getClass(), this);
        assertNotNull(testedService);
        testedService.assertInjection();
    }

    @Test
    public void injected_mocks_and_spies_should_be_of_same_instance() {
        new InjectingAnnotationEngine().process(getClass(), this);
        assertNotNull(testedService);
        testedService.assertInjection();
        assertEquals(spiedService1, testedService.spiedService1);
        assertEquals(spiedService1, testedService.spiedService2.spiedService1);
        assertEquals(spiedService2, testedService.spiedService2);
        assertEquals(spiedService2, testedService.spiedService1.spiedService2);
    }

}
