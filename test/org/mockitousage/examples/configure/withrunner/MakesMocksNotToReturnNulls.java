/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure.withrunner;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.mockito.configuration.experimental.ConfigurationSupport;
import org.mockitousage.examples.configure.FriendlyReturnValues;

public class MakesMocksNotToReturnNulls extends BlockJUnit4ClassRunner {
    
    public MakesMocksNotToReturnNulls(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
    
    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        //setting up custom return values
        ConfigurationSupport.getConfiguration().setReturnValues(new FriendlyReturnValues());
        return test;
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        //I don't want mocks from other tests to be reconfigured
        ConfigurationSupport.getConfiguration().resetReturnValues();
    }
}