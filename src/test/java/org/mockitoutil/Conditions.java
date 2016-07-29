/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitoutil;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.hamcrest.CoreMatchers;

import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class Conditions {

    public static Condition<Throwable> onlyThoseClassesInStackTrace(final String... classes) {
        return new Condition<Throwable>() {
            @Override
            public boolean matches(Throwable traceElements) {
                StackTraceElement[] trace = traceElements.getStackTrace();

                Assertions.assertThat(trace.length)
                          .describedAs("Number of classes does not match.\nExpected: %s\nGot: %s",
                                       Arrays.toString(classes),
                                       Arrays.toString(traceElements.getStackTrace()))
                          .isEqualTo(classes.length);

                for (int i = 0; i < trace.length; i++) {
                    Assertions.assertThat(trace[i].getClassName()).isEqualTo(classes[i]);
                }

                return true;
            }
        };
    }

    public static Condition<StackTraceElement[]> onlyThoseClasses(final String... classes) {
        return new Condition<StackTraceElement[]>() {

            @Override
            public boolean matches(StackTraceElement[] traceElements) {
                Assertions.assertThat(traceElements.length)
                          .describedAs("Number of classes does not match.\nExpected: %s\nGot: %s",
                                       Arrays.toString(classes),
                                       Arrays.toString(traceElements))
                          .isEqualTo(classes.length);

                for (int i = 0; i < traceElements.length; i++) {
                    Assertions.assertThat(traceElements[i].getClassName()).isEqualTo(classes[i]);
                }

                return true;
            }
        };
    }

    public static Condition<Throwable> firstMethodInStackTrace(final String method) {
        return methodInStackTraceAt(0, method);
    }

    public static Condition<Throwable> methodInStackTraceAt(final int stackTraceIndex, final String method) {
        return new Condition<Throwable>() {
            private String actualMethodAtIndex;

            @Override
            public boolean matches(Throwable throwable) {
                actualMethodAtIndex = throwable.getStackTrace()[stackTraceIndex].getMethodName();

                return actualMethodAtIndex.equals(method);
            }

            @Override
            public Description description() {
                return new TextDescription("Method at index: %d\nexpected to be: %s\nbut is: %s", stackTraceIndex, method, actualMethodAtIndex);
            }
        };
    }

    public static Condition<Object> bridgeMethod(final String methodName) {
        return new Condition<Object>() {

            public boolean matches(Object o) {
                Class<?> clazz = null;
                if (o instanceof Class) {
                    clazz = (Class<?>) o;
                } else {
                    clazz = o.getClass();
                }

                for (Method m : clazz.getMethods()) {
                    if (m.isBridge() && m.getName().equals(methodName)) {
                        return true;
                    }
                }

                Assertions.fail("Bridge method [" + methodName + "]\nnot found in:\n" + o);
                return false;
            }
        };
    }

    public static org.hamcrest.Matcher<Object> clazz(Class<?> type) {
        return CoreMatchers.instanceOf(type);
    }

    public static Condition<Throwable> methodsInStackTrace(final String... methods) {
        return new Condition<Throwable>() {
            public boolean matches(Throwable value) {
                StackTraceElement[] trace = value.getStackTrace();
                for (int i = 0; i < methods.length; i++) {
                    Assertions.assertThat(trace[i].getMethodName()).describedAs("Expected methods[%d] to be in the stack trace.", i).isEqualTo(methods[i]);
                }
                return true;
            }
        };
    }


}