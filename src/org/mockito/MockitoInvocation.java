package org.mockito;

import java.lang.reflect.Method;
import java.util.List;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.*;

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
