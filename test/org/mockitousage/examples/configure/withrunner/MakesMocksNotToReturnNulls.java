package org.mockitousage.examples.configure.withrunner;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.DefaultReturnValues;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class MakesMocksNotToReturnNulls extends JUnit4ClassRunner {
    
    public MakesMocksNotToReturnNulls(Class<?> klass) throws InitializationError {
        super(klass);
    }
    
    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        //setting up custom return values
        MockitoConfiguration.instance().setReturnValues(new MyDefaultReturnValues());
        //initializing annotated mocks
        MockitoAnnotations.initMocks(test);
        return test;
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        MockitoConfiguration.instance().resetReturnValues();
    }
    
    private final class MyDefaultReturnValues implements ReturnValues {
        public Object valueFor(InvocationOnMock invocation) {
            //get the default return value
            Object value = new DefaultReturnValues().valueFor(invocation);
            if (value != null || invocation.getMethod().getReturnType() == Void.TYPE) {
                return value;
            } else {
                //in case the default return value is null and method is not void, return new mock
                return Mockito.mock(invocation.getMethod().getReturnType());
            }
        }
    }
}