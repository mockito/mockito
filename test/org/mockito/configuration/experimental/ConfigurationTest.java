/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration.experimental;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Test;
import org.mockito.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ConfigurationTest extends TestBase {
    
    @SuppressWarnings("deprecation")
    @Test
    public void shouldNotAllowConfiguringWithNullReturnValues() {
        try {
            ConfigurationSupport.getConfiguration().setReturnValues(null);
            fail();
        } catch (MockitoException e) {
            assertThat(e, messageContains("Cannot set null ReturnValues!"));
        }
    }
   
    @Test
    public void shouldReadConfigurationClassFromClassPath() {
        MockitoConfiguration.overrideReturnValues(new ReturnValues() {
            public Object valueFor(InvocationOnMock invocation) {
                return "foo";
            }});

        IMethods mock = mock(IMethods.class); 
        assertEquals("foo", mock.simpleMethod());
    }
    
    @After
    public void cleanUpConfig() {
        MockitoConfiguration.overrideReturnValues(null);
    }
}