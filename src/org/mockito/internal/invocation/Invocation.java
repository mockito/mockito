/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.*;

import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;

/**
 * Method call on a mock object.
 * <p>
 * Contains sequence number which should be globally unique and is used for
 * verification in order.
 * <p>
 * Contains stack trace of invocation
 */
@SuppressWarnings("unchecked")
public class Invocation implements PrintableInvocation, InvocationOnMock, PrintingFriendlyInvocation {

    private static final long serialVersionUID = 8240069639250980199L;
    private static final int MAX_LINE_LENGTH = 45;
    private final int sequenceNumber;
    private final Object mock;
    private final MockitoMethod method;
    private final Object[] arguments;
    private final Object[] rawArguments;

    private final Location location;
    private boolean verified;
    private boolean verifiedInOrder;

    final RealMethod realMethod;

    public Invocation(Object mock, MockitoMethod mockitoMethod, Object[] args, int sequenceNumber, RealMethod realMethod) {
        this.method = mockitoMethod;
        this.mock = mock;
        this.realMethod = realMethod;
        this.arguments = expandVarArgs(mockitoMethod.isVarArgs(), args);
        this.rawArguments = args;
        this.sequenceNumber = sequenceNumber;
        this.location = new Location();
    }

    // expands array varArgs that are given by runtime (1, [a, b]) into true
    // varArgs (1, a, b);
    private static Object[] expandVarArgs(final boolean isVarArgs, final Object[] args) {
        if (!isVarArgs || args[args.length - 1] != null && !args[args.length - 1].getClass().isArray()) {
            return args == null ? new Object[0] : args;
        }

        final int nonVarArgsCount = args.length - 1;
        Object[] varArgs;
        if (args[nonVarArgsCount] == null) {
            // in case someone deliberately passed null varArg array
            varArgs = new Object[] { null };
        } else {
            varArgs = ArrayEquals.createObjectArray(args[nonVarArgsCount]);
        }
        final int varArgsCount = varArgs.length;
        Object[] newArgs = new Object[nonVarArgsCount + varArgsCount];
        System.arraycopy(args, 0, newArgs, 0, nonVarArgsCount);
        System.arraycopy(varArgs, 0, newArgs, nonVarArgsCount, varArgsCount);
        return newArgs;
    }

    public Object getMock() {
        return mock;
    }

    public MockitoMethod getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public boolean isVerified() {
        return verified;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public boolean isVerifiedInOrder() {
        return verifiedInOrder;
    }

    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }

        Invocation other = (Invocation) o;

        return this.mock.equals(other.mock) && this.method.equals(other.method) && this.equalArguments(other.arguments);
    }

    private boolean equalArguments(Object[] arguments) {
        return Arrays.equals(arguments, this.arguments);
    }

    public int hashCode() {
        throw new RuntimeException("hashCode() is not implemented");
    }

    public String toString() {
        return toString(argumentsToMatchers(), new PrintSettings());
    }

    protected String toString(List<Matcher> matchers, PrintSettings printSettings) {
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String method = qualifiedMethodName();
        String invocation = method + matchersPrinter.getArgumentsLine(matchers, printSettings);
        if (printSettings.isMultiline() || (!matchers.isEmpty() && invocation.length() > MAX_LINE_LENGTH)) {
            return method + matchersPrinter.getArgumentsBlock(matchers, printSettings);
        } else {
            return invocation;
        }
    }

    private String qualifiedMethodName() {
        return new MockUtil().getMockName(mock) + "." + method.getName();
    }

    protected List<Matcher> argumentsToMatchers() {
        List<Matcher> matchers = new ArrayList<Matcher>(arguments.length);
        for (Object arg : arguments) {
            if (arg != null && arg.getClass().isArray()) {
                matchers.add(new ArrayEquals(arg));
            } else {
                matchers.add(new Equals(arg));
            }
        }
        return matchers;
    }

    public static boolean isToString(InvocationOnMock invocation) {
        return new ObjectMethodsGuru().isToString(invocation.getMethod());
    }

    public boolean isValidException(Throwable throwable) {
        Class<?>[] exceptions = this.getMethod().getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidReturnType(Class clazz) {
        if (method.getReturnType().isPrimitive()) {
            return Primitives.primitiveTypeOf(clazz) == method.getReturnType();
        } else {
            return method.getReturnType().isAssignableFrom(clazz);
        }
    }

    public boolean isVoid() {
        return this.method.getReturnType() == Void.TYPE;
    }

    public String printMethodReturnType() {
        return method.getReturnType().getSimpleName();
    }

    public String getMethodName() {
        return method.getName();
    }

    public boolean returnsPrimitive() {
        return method.getReturnType().isPrimitive();
    }

    public Location getLocation() {
        return location;
    }

    public int getArgumentsCount() {
        return arguments.length;
    }

    public Object[] getRawArguments() {
        return this.rawArguments;
    }

    public Object callRealMethod() throws Throwable {
        return realMethod.invoke(mock, rawArguments);
    }

    public String toString(PrintSettings printSettings) {
        return toString(argumentsToMatchers(), printSettings);
    }

    void markVerified() {
        this.verified = true;
    }

    void markVerifiedInOrder() {
        markVerified();
        this.verifiedInOrder = true;
    }
}