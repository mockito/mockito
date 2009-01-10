package org.mockito;

import org.mockito.internal.returnvalues.EmptyReturnValues;

//TODO javadocs
public class DefaultMockitoConfiguration implements IMockitoConfiguration {

    public ReturnValues getReturnValues() {
        return new EmptyReturnValues();
    }
}