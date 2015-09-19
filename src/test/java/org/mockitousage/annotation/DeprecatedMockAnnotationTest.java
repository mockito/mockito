/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.awt.*;

import static org.junit.Assert.assertNotNull;

public class DeprecatedMockAnnotationTest {

    @MockitoAnnotations.Mock private List deprecatedMock;

    @InjectMocks private AnInjectedObject anInjectedObject;

    @Before
    public void initMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateMockForDeprecatedMockAnnotation() throws Exception {
        assertNotNull(deprecatedMock);
    }

    @Test
    public void shouldInjectDeprecatedMockAnnotation() throws Exception {
        assertNotNull(anInjectedObject.aFieldAwaitingInjection);
    }

    private static class AnInjectedObject {
        List aFieldAwaitingInjection;
    }
}
