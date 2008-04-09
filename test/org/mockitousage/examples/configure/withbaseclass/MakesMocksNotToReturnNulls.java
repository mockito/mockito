/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure.withbaseclass;

import org.junit.After;
import org.junit.Before;
import org.mockito.configuration.MockitoConfiguration;
import org.mockitousage.examples.configure.FriendlyReturnValues;

public class MakesMocksNotToReturnNulls {
    
    @Before
    public void configureMockito() {
        //setting custom return values
        MockitoConfiguration.instance().setReturnValues(new FriendlyReturnValues());
    }
    
    @After
    public void resetReturnValuesToDefaults() {
        //I don't want mocks from other tests to be reconfigured
        MockitoConfiguration.instance().resetReturnValues();
    }
}