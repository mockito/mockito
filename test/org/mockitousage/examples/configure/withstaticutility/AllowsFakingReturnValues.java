package org.mockitousage.examples.configure.withstaticutility;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.mockito.configuration.BaseReturnValues;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class AllowsFakingReturnValues {
    
    public static void fakeReturnValues(Object ... mocks) {
        FakeReturnValues fakeReturnValues = getFakeReturnValues();
        fakeReturnValues.configure(mocks);
    }
    
    private static FakeReturnValues getFakeReturnValues() {
        MockitoConfiguration config = MockitoConfiguration.instance();
        ReturnValues current = config.getReturnValues();
        //if my custom return values are NOT yet set, do it 
        if (!(current instanceof FakeReturnValues)) {
            config.setReturnValues(new FakeReturnValues());
        }
        return (FakeReturnValues) config.getReturnValues();
    }

    private static final class FakeReturnValues extends BaseReturnValues {
        
        private Set<Object> mocksReturningFakes = new HashSet<Object>();

        public Object returnValueFor(InvocationOnMock invocation) {
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (mocksReturningFakes.contains(invocation.getMock())) {
                return returnFake(returnType);
            } else {
                return null;
            }
        }

        public void configure(Object ... mocks) {
            mocksReturningFakes.addAll(Arrays.asList(mocks));
        }

        private Object returnFake(Class<?> returnType) {
            if (returnType == String.class) {
                return "";
            } else if (returnType == Boolean.TYPE) {
                return true;
            } else {
                return mock(returnType);
            }
        }
    }
}
