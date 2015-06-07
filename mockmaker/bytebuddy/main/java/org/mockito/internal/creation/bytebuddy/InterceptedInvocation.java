package org.mockito.internal.creation.bytebuddy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.StubInfo;

@SuppressWarnings("rawtypes")
class InterceptedInvocation implements Invocation, VerificationAwareInvocation {

    private static final long serialVersionUID = 475027563923510472L;

    private final Object mock;
    private final MockitoMethod mockitoMethod;
    private final Object[] arguments, rawArguments;
    private final SuperMethod superMethod;

    private final int sequenceNumber;

    private final Location location;

    private boolean verified;
    private boolean isIgnoredForVerification;
    private StubInfo stubInfo;

    public InterceptedInvocation(final Object mock,
                                 final MockitoMethod mockitoMethod,
                                 final Object[] arguments,
                                 final SuperMethod superMethod,
                                 final int sequenceNumber) {
        this.mock = mock;
        this.mockitoMethod = mockitoMethod;
        this.arguments = ArgumentsProcessor.expandVarArgs(mockitoMethod.isVarArgs(), arguments);
        this.rawArguments = arguments;
        this.superMethod = superMethod;
        this.sequenceNumber = sequenceNumber;
        location = new LocationImpl();
    }

    
    public boolean isVerified() {
        return verified || isIgnoredForVerification;
    }

    
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    
    public Location getLocation() {
        return location;
    }

    
    public Object[] getRawArguments() {
        return rawArguments;
    }

    
    public Class getRawReturnType() {
        return mockitoMethod.getReturnType();
    }

    
    public void markVerified() {
        verified = true;
    }

    
    public StubInfo stubInfo() {
        return stubInfo;
    }

    
    public void markStubbed(final StubInfo stubInfo) {
        this.stubInfo = stubInfo;
    }

    
    public boolean isIgnoredForVerification() {
        return isIgnoredForVerification;
    }

    
    public void ignoreForVerification() {
        isIgnoredForVerification = true;
    }

    
    public Object getMock() {
        return mock;
    }

    
    public Method getMethod() {
        return mockitoMethod.getJavaMethod();
    }

    
    public Object[] getArguments() {
        return arguments;
    }

    
    @SuppressWarnings("unchecked")
    public <T> T getArgumentAt(final int index, final Class<T> clazz) {
        return (T) arguments[index];
    }

    
    public Object callRealMethod() throws Throwable {
        if (!superMethod.isInvokable()) {
            new Reporter().cannotCallAbstractRealMethod();
        }
        return superMethod.invoke();
    }

    
    public int hashCode() {
        return 1;
    }

    
    public boolean equals(final Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }
        final InterceptedInvocation other = (InterceptedInvocation) o;
        return this.mock.equals(other.mock)
                && this.mockitoMethod.equals(other.mockitoMethod)
                && this.equalArguments(other.arguments);
    }

    private boolean equalArguments(final Object[] arguments) {
        return Arrays.equals(arguments, this.arguments);
    }

    public String toString() {
        return new PrintSettings().print(ArgumentsProcessor.argumentsToMatchers(getArguments()), this);
    }


    public static interface SuperMethod extends Serializable {

        static enum IsIllegal implements SuperMethod {

            INSTANCE;

            
            public boolean isInvokable() {
                return false;
            }

            
            public Object invoke() {
                throw new IllegalStateException();
            }
        }

        static class FromCallable implements SuperMethod {

            private static final long serialVersionUID = 47957363950483625L;

            private final Callable<?> callable;

            public FromCallable(final Callable<?> callable) {
                this.callable = callable;
            }

            
            public boolean isInvokable() {
                return true;
            }

            
            public Object invoke() throws Throwable {
                try {
                    return callable.call();
                } catch (final Throwable t) {
                    new ConditionalStackTraceFilter().filter(t);
                    throw t;
                }
            }
        }

        boolean isInvokable();

        Object invoke() throws Throwable;
    }

}
