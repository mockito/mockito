package org.mockitousage.stubbing;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.configuration.ReturnValues;
import org.mockito.configuration.experimental.ConfigurationSupport;
import org.mockito.internal.returnvalues.GloballyConfiguredReturnValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;


public class GloballyConfiguredReturnValuesTest extends TestBase {
    
    public class HardCodedReturnValues implements ReturnValues {
        private final String returnValue;

        public HardCodedReturnValues(String returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public Object valueFor(InvocationOnMock invocation) {
            return returnValue;
        }
    }

    @Test
    public void shouldUseCurrentlyConfiguredReturnValuesEvenIfTheyChangeAtRuntime() throws Exception {
        ReturnValues returnsEdam = new HardCodedReturnValues("edam");
        ReturnValues returnsCheddar = new HardCodedReturnValues("cheddar");
        IMethods mock = Mockito.mock(IMethods.class, new GloballyConfiguredReturnValues());
        
        ConfigurationSupport.getConfiguration().setReturnValues(returnsEdam);
        
        assertEquals("edam", mock.simpleMethod());
        
        ConfigurationSupport.getConfiguration().setReturnValues(returnsCheddar);
        
        assertEquals("cheddar", mock.simpleMethod());
    }
    
    @After
    public void resetReturnValues() {
        ConfigurationSupport.getConfiguration().resetReturnValues();
    }
}