package org.mockito.internal;

import java.util.LinkedList;

import org.mockito.exceptions.Exceptions;

public class Stubber {

    private ExpectedInvocation invocationForStubbing;
    private LinkedList<StubbedInvocation> stubbed = new LinkedList<StubbedInvocation>();
    private Throwable throwableForVoidMethod;
    
    public void addReturnValue(Object value) {
        MockitoState.instance().stubbingCompleted();
        addResult(Result.createReturnResult(value));
    }
    
    public void addThrowable(Throwable throwable) {
        MockitoState.instance().stubbingCompleted();
        validateThrowable(throwable);
        addResult(Result.createThrowResult(throwable));
    }

    public void addVoidMethodForThrowable(ExpectedInvocation invocationWithMatchers) {
        this.invocationForStubbing = invocationWithMatchers;
        this.addThrowable(throwableForVoidMethod);
        throwableForVoidMethod = null;
    }
    
    private void addResult(Result result) {
        assert invocationForStubbing != null;
        stubbed.addFirst(new StubbedInvocation(invocationForStubbing, result));
    }
    
    public Object resultFor(Invocation wanted) throws Throwable {
        for (StubbedInvocation s : stubbed) {
            if (s.matches(wanted)) {
                return s.getResult().answer();
            }
        }

        return ToTypeMappings.emptyReturnValueFor(wanted.getMethod().getReturnType());
    }
    
    private void validateThrowable(Throwable throwable) {
        if (throwable == null) {
            Exceptions.cannotStubWithNullThrowable();
        }

        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }
    
        if (!isValidCheckedException(throwable)) {
            Exceptions.checkedExceptionInvalid(throwable);
        }
    }

    private boolean isValidCheckedException(Throwable throwable) {
        Invocation lastInvocation = invocationForStubbing.getInvocation();

        Class<?>[] exceptions = lastInvocation.getMethod().getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }
        
        return false;
    }

    public void setInvocationForPotentialStubbing(ExpectedInvocation invocation) {
        this.invocationForStubbing = invocation;
    }

    public void addThrowableForVoidMethod(Throwable throwable) {
        throwableForVoidMethod = throwable;
    }

    public boolean hasThrowableForVoidMethod() {
        return throwableForVoidMethod != null;
    }
}
