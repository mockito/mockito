package org.mockito;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.internal.configuration.DefaultReturnValues;

//TODO javadocs
//TODO test
public class DefaultMockitoConfiguration implements IMockitoConfiguration {

    public ReturnValues getReturnValues() {
        return new DefaultReturnValues();
    }
}