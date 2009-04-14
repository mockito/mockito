/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.configuration;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ReturnValues;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockito.invocation.InvocationOnMock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class MockitoConfigurationTest extends TestBase {
    
    @Test
    public void shouldReadConfigurationClassFromClassPath() {
        ConfigurationAccess.getConfig().overrideReturnValues(new ReturnValues() {
            public Object valueFor(InvocationOnMock invocation) {
                return "foo";
            }});

        IMethods mock = mock(IMethods.class); 
        assertEquals("foo", mock.simpleMethod());
    }
    
    @SmartMock IMethods smartMock;

    @Test
    public void shouldUseCustomAnnotation() {
        assertEquals("SmartMock should return empty String by default", "", smartMock.simpleMethod(1));
        verify(smartMock).simpleMethod(1);
    }
}