package org.mockito.internal.creation.bytebuddy;

import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.StubInfo;

import static org.mockito.internal.exceptions.Reporter.cannotCallAbstractRealMethod;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

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

    public InterceptedInvocation(Object mock,
                                 MockitoMethod mockitoMethod,
                                 Object[] arguments,
                                 SuperMethod superMethod,
                                 int sequenceNumber) {
        this.mock = mock;
        this.mockitoMethod = mockitoMethod;
        this.arguments = ArgumentsProcessor.expandVarArgs(mockitoMethod.isVarArgs(), arguments);
        this.rawArguments = arguments;
        this.superMethod = superMethod;
        this.sequenceNumber = sequenceNumber;
        location = new LocationImpl();
    }

    @Override
    public boolean isVerified() {
        return verified || isIgnoredForVerification;
    }

    @Override
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Object[] getRawArguments() {
        return rawArguments;
    }

    @Override
    public Class<?> getRawReturnType() {
        return mockitoMethod.getReturnType();
    }

    @Override
    public void markVerified() {
        verified = true;
    }

    @Override
    public StubInfo stubInfo() {
        return stubInfo;
    }

    @Override
    public void markStubbed(StubInfo stubInfo) {
        this.stubInfo = stubInfo;
    }

    @Override
    public boolean isIgnoredForVerification() {
        return isIgnoredForVerification;
    }

    @Override
    public void ignoreForVerification() {
        isIgnoredForVerification = true;
    }

    @Override
    public Object getMock() {
        return mock;
    }

    @Override
    public Method getMethod() {
        return mockitoMethod.getJavaMethod();
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getArgument(int index) {
        return (T) arguments[index];
    }

    @Override
    public Object callRealMethod() throws Throwable {
        if (!superMethod.isInvokable()) {
            throw cannotCallAbstractRealMethod();
        }
        return superMethod.invoke();
    }

    @Override
    public int hashCode() {
        //TODO SF we need to provide hash code implementation so that there are no unexpected, slight perf issues
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }
        InterceptedInvocation other = (InterceptedInvocation) o;
        return this.mock.equals(other.mock)
                && this.mockitoMethod.equals(other.mockitoMethod)
                && this.equalArguments(other.arguments);
    }

    private boolean equalArguments(Object[] arguments) {
        return Arrays.equals(arguments, this.arguments);
    }

    public String toString() {
        return new PrintSettings().print(ArgumentsProcessor.argumentsToMatchers(getArguments()), this);
    }


    public interface SuperMethod extends Serializable {

        enum IsIllegal implements SuperMethod {

            INSTANCE;

            @Override
            public boolean isInvokable() {
                return false;
            }

            @Override
            public Object invoke() {
                throw new IllegalStateException();
            }
        }

        class FromCallable implements SuperMethod {

            private static final long serialVersionUID = 47957363950483625L;

            private final Callable<?> callable;

            public FromCallable(Callable<?> callable) {
                this.callable = callable;
            }

            @Override
            public boolean isInvokable() {
                return true;
            }

            @Override
            public Object invoke() throws Throwable {
                try {
                    return callable.call();
                } catch (Throwable t) {
                    new ConditionalStackTraceFilter().filter(t);
                    throw t;
                }
            }
        }

        boolean isInvokable();

        Object invoke() throws Throwable;
    }

}
