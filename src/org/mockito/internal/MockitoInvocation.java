package org.mockito.internal;

import java.lang.reflect.Method;


//TODO kill this class and move verified to InvocationWithMatchers
public class MockitoInvocation extends Invocation {

    private boolean verified;

    public MockitoInvocation(Object mock, Method method, Object[] args) {
        super(mock, method, args);
    }

    public void markVerified() {
        verified = true;
    }

    public boolean isVerified() {
        return verified;
    }
    
    @Override
    public int hashCode() {
        return 1;
    }
}
