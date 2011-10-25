/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.configuration;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class CustomizedAnnotationForSmartMockTest extends TestBase {
    
    @SmartMock IMethods smartMock;

    @Test
    public void shouldUseCustomAnnotation() {
        assertEquals("SmartMock should return empty String by default", "", smartMock.simpleMethod(1));
        verify(smartMock).simpleMethod(1);
    }
}