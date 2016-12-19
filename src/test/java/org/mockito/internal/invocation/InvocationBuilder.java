/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import org.mockito.Mockito;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.util.reflection.AccessibilityChanger;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockitousage.IMethods;

import static java.util.Arrays.asList;

/**
 * Build an invocation.
 */
@SuppressWarnings("unchecked")
public class InvocationBuilder {

    private String methodName = "simpleMethod";
    private int sequenceNumber = 0;
    private Object[] args = new Object[]{};
    private Object mock = Mockito.mock(IMethods.class);
    private Method method;
    private boolean verified;
    private List<Class<?>> argTypes;
    private Location location;

    /**
     * Build the invocation
     * <p>
     * If the method was not specified, use IMethods methods.
     *
     * @return invocation
     */
    public Invocation toInvocation() {
        if (method == null) {
            if (argTypes == null) {
                argTypes = new LinkedList<Class<?>>();
                for (Object arg : args) {
                    if (arg == null) {
                        argTypes.add(Object.class);
                    } else {
                        argTypes.add(arg.getClass());
                    }
                }
            }

            try {
                method = IMethods.class.getMethod(methodName, argTypes.toArray(new Class[argTypes.size()]));
            } catch (Exception e) {
                throw new RuntimeException("builder only creates invocations of IMethods interface", e);
            }
        }

        Invocation i = new InvocationImpl(mock,
                                          new SerializableMethod(method),
                                          args,
                                          sequenceNumber,
                                          toDumbRealMethod(),
                                          location == null ? new LocationImpl() : location);
        if (verified) {
            i.markVerified();
        }
        return i;
    }

    private RealMethod toDumbRealMethod() {
        return new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] arguments) throws Throwable {
                AccessibilityChanger accessibilityChanger = new AccessibilityChanger();
                try {
                    accessibilityChanger.enableAccess(method);
                    return method.invoke(target, arguments);
                } finally {
                    accessibilityChanger.safelyDisableAccess(method);
                }

            }
        };
    }

    public InvocationBuilder method(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public InvocationBuilder seq(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public InvocationBuilder args(Object... args) {
        this.args = args;
        return this;
    }

    public InvocationBuilder arg(Object o) {
        this.args = new Object[]{o};
        return this;
    }

    public InvocationBuilder mock(Object mock) {
        this.mock = mock;
        return this;
    }

    public InvocationBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public InvocationBuilder verified() {
        this.verified = true;
        return this;
    }

    public InvocationMatcher toInvocationMatcher() {
        return new InvocationMatcher(toInvocation());
    }

    public InvocationBuilder simpleMethod() {
        return this.method("simpleMethod");
    }

    public InvocationBuilder differentMethod() {
        return this.method("differentMethod");
    }

    public InvocationBuilder argTypes(Class<?>... argTypes) {
        this.argTypes = asList(argTypes);
        return this;
    }

    public InvocationBuilder location(final String location) {
        this.location = new Location() {
            public String toString() {
                return location;
            }
        };
        return this;
    }
}
