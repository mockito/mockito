/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

public class InjectingAnnotationEngineCircularInjectionByConstructorTest {

    public static class TestedService {
        final SpiedService1 service1;
        final SpiedService2 service2;
        public TestedService(SpiedService1 service1, SpiedService2 service2) {
            this.service1 = service1;
            this.service2 = service2;
        }
    }

    public static class SpiedService1 {
        final SpiedService2 service2;
        public SpiedService1(SpiedService2 service2) {
            this.service2 = service2;
        }
    }

    public static class SpiedService2 {
        final SpiedService1 service1;
        public SpiedService2(SpiedService1 service) {
            this.service1 = service;
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
    public void should_fail_to_inject_circular_dependencies_by_constructor() {
        new InjectingAnnotationEngine().process(getClass(), this);
        Assert.assertNotNull(testedService);
        Assert.assertNull(testedService.service1);
        Assert.assertNull(testedService.service2);
        Assert.assertNotNull(spiedService1);
        Assert.assertNull(spiedService1.service2);
        Assert.assertNotNull(spiedService2);
        Assert.assertNull(spiedService2.service1);
    }

}
