package org.mockito;

import org.mockito.internal.configuration.Configuration;

@SuppressWarnings("deprecation")
public class MockitoConfiguration implements IMockitoConfiguration {

    private static ReturnValues overridden = null;

    //for testing purposes, allow to override the configuration
    public static void overrideReturnValues(ReturnValues returnValues) {
        MockitoConfiguration.overridden = returnValues;
    }

    public ReturnValues getReturnValues() {
        if (overridden == null) {
            return Configuration.instance().getReturnValues();
        } else {
            return overridden;
        }
    }
}