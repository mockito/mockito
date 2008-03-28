package org.mockitousage.examples.configure;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockito.configuration.DefaultReturnValues;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class MakesMocksNotToReturnNulls extends TestBase {
    
    @Before
    public void configureDefaultReturnValues() {
        MockitoConfiguration.instance().setReturnValues(new MyDefaultReturnValues());
    }
    
    @After
    public void resetReturnValuesToDefaults() {
        MockitoConfiguration.instance().resetReturnValues();
    }
    
    private final class MyDefaultReturnValues implements ReturnValues {
        public Object valueFor(InvocationOnMock invocation) {
            //get the default return value
            Object value = new DefaultReturnValues().valueFor(invocation);
            if (value != null || invocation.getMethod().getReturnType() == Void.TYPE) {
                return value;
            } else {
                //in case the default return value is null and method is not void, return new mock:
                return Mockito.mock(invocation.getMethod().getReturnType());
            }
        }
    }
}