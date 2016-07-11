/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.*;

import static org.mockito.internal.exceptions.Reporter.cannotCallAbstractRealMethod;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Method call on a mock object.
 * <p>
 * Contains sequence number which should be globally unique and is used for
 * verification in order.
 * <p>
 * Contains stack trace of invocation
 */
@SuppressWarnings("unchecked")
public class InvocationImpl implements Invocation, VerificationAwareInvocation {

    private static final long serialVersionUID = 8240069639250980199L;
    private final int sequenceNumber;
    private final Object mock;
    private final MockitoMethod method;
    private final Object[] arguments;
    private final Object[] rawArguments;

    private final Location location;
    private boolean verified;
    private boolean isIgnoredForVerification;

    final RealMethod realMethod;
    private StubInfo stubInfo;

    public InvocationImpl(Object mock, MockitoMethod mockitoMethod, Object[] args, int sequenceNumber,
                          RealMethod realMethod, Location location) {
        this.method = mockitoMethod;
        this.mock = mock;
        this.realMethod = realMethod;
        this.arguments = ArgumentsProcessor.expandVarArgs(mockitoMethod.isVarArgs(), args);
        this.rawArguments = args;
        this.sequenceNumber = sequenceNumber;
        this.location = location;
    }

    public Object getMock() {
        return mock;
    }

    public Method getMethod() {
        return method.getJavaMethod();
    }

    public Object[] getArguments() {
        return arguments;
    }

    public <T> T getArgument(int index) {
        return (T)arguments[index];
    }

    public boolean isVerified() {
        return verified || isIgnoredForVerification;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }

        InvocationImpl other = (InvocationImpl) o;

        return this.mock.equals(other.mock) && this.method.equals(other.method) && this.equalArguments(other.arguments);
    }

    private boolean equalArguments(Object[] arguments) {
        return Arrays.equals(arguments, this.arguments);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public String toString() {
        return new PrintSettings().print(ArgumentsProcessor.argumentsToMatchers(getArguments()), this);
    }

    public Location getLocation() {
        return location;
    }

    public Object[] getRawArguments() {
        return this.rawArguments;
    }

    public Class<?> getRawReturnType() {
        return method.getReturnType();
    }

    public Object callRealMethod() throws Throwable {
        if (method.isAbstract()) {
            throw cannotCallAbstractRealMethod();
        }
        return realMethod.invoke(mock, rawArguments);
    }

    public void markVerified() {
        this.verified = true;
    }

    public StubInfo stubInfo() {
        return stubInfo;
    }

    public void markStubbed(StubInfo stubInfo) {
        this.stubInfo = stubInfo;
    }

    public boolean isIgnoredForVerification() {
        return isIgnoredForVerification;
    }

    public void ignoreForVerification() {
        isIgnoredForVerification = true;
    }
}
