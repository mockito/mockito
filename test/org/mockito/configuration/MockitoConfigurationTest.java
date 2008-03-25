/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.base.MockitoException;

public class MockitoConfigurationTest extends TestBase {
    
    @Test
    public void shouldNotAllowConfiguringWithNullReturnValues() {
        try {
            MockitoConfiguration.instance().setReturnValues(null);
            fail();
        } catch (MockitoException e) {
            assertContains("Cannot set null ReturnValues!", e.getMessage());
        }
    }
}
