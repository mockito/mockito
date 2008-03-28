package org.mockitousage.examples.configure;

import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.mockito.TestBase;
import org.mockito.configuration.DefaultReturnValues;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class CanMakeMocksSmart extends TestBase {

    private MyDefaultReturnValues myDefaultReturnValues;

    protected void beSmart(Object mock) {
        myDefaultReturnValues.addSmartMock(mock);
    }
    
    @Before
    public void configureDefaultReturnValues() {
        myDefaultReturnValues = new MyDefaultReturnValues();
        MockitoConfiguration.instance().setReturnValues(myDefaultReturnValues);
    }
    
    @After
    public void resetReturnValuesToDefaults() {
        MockitoConfiguration.instance().resetReturnValues();
    }
    
    //My own sophisticated version of ReturnValues - it treats 'smart mocks' differently
    private final class MyDefaultReturnValues implements ReturnValues {
        private List<Object> smartMocks = new LinkedList<Object>();

        public Object valueFor(InvocationOnMock invocation) {
            Object value = new DefaultReturnValues().valueFor(invocation);
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (value != null || returnType == Void.TYPE) {
                return value;
            } else if (smartMocks.contains(invocation.getMock())) {
                //if is a smart mock then return different value
                return returnValueForSmartMock(returnType);
            } else {
                return null;
            }
        }

        private Object returnValueForSmartMock(Class<?> returnType) {
            if (returnType == String.class) {
                return "";
            } else if (returnType == Boolean.TYPE) {
                return true;
            } else {
                return mock(returnType);
            }
        }

        public void addSmartMock(Object mock) {
            smartMocks.add(mock);
        }
    }
}
