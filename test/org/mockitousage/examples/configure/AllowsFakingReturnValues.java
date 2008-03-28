package org.mockitousage.examples.configure;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.mockito.TestBase;
import org.mockito.configuration.DefaultReturnValues;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class AllowsFakingReturnValues extends TestBase {
    
    protected void fakeReturnValues(Object ... mocks) {
        FakeReturnValues fakeReturnValues = getFakeReturnValues();
        fakeReturnValues.configure(mocks);
    }
    
    private FakeReturnValues getFakeReturnValues() {
        MockitoConfiguration config = MockitoConfiguration.instance();
        ReturnValues current = config.getReturnValues();
        if (!(current instanceof FakeReturnValues)) {
            config.setReturnValues(new FakeReturnValues());
        }
        return (FakeReturnValues) config.getReturnValues();
    }

    private final class FakeReturnValues implements ReturnValues {
        private Set<Object> mocksReturningFakes = new HashSet<Object>();

        public Object valueFor(InvocationOnMock invocation) {
            Object value = new DefaultReturnValues().valueFor(invocation);
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (value != null || returnType == Void.TYPE) {
                return value;
            } else if (mocksReturningFakes.contains(invocation.getMock())) {
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
