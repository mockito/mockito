/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration.experimental;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

public class ConfigurationTest extends TestBase {
    
    @Test
    public void shouldNotAllowConfiguringWithNullReturnValues() {
        try {
            ConfigurationSupport.getConfiguration().setReturnValues(null);
            fail();
        } catch (MockitoException e) {
            assertThat(e, messageContains("Cannot set null ReturnValues!"));
        }
    }
}
