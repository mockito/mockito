/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitoutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

@SuppressWarnings("rawtypes")
public class ExtraMatchers {

    public static <T> Assertor<Throwable> hasFirstMethodInStackTrace(final String method) {
        return hasMethodInStackTraceAt(0, method);
    }
    
    public static <T> Assertor<Throwable> hasOnlyThoseClassesInStackTrace(final String ... classes) {
        return new Assertor<Throwable>() {
            public void assertValue(final Throwable traceElements) {
                final StackTraceElement[] trace = traceElements.getStackTrace();
                
                assertEquals("Number of classes does not match." +
                        "\nExpected: " + Arrays.toString(classes) + 
                        "\nGot: " + Arrays.toString(traceElements.getStackTrace()),
                        classes.length, trace.length);
                    
                for (int i = 0; i < trace.length; i++) {
                    assertEquals(classes[i], trace[i].getClassName());
                }
            }
        };
    }
    
    public static <T> Assertor<StackTraceElement[]> hasOnlyThoseClasses(final String ... classes) {
        return new Assertor<StackTraceElement[]>() {
            public void assertValue(final StackTraceElement[] traceElements) {
                assertEquals("Number of classes does not match." +
                        "\nExpected: " + Arrays.toString(classes) + 
                        "\nGot: " + Arrays.toString(traceElements),
                        classes.length, traceElements.length);
                
                for (int i = 0; i < traceElements.length; i++) {
                    assertEquals(classes[i], traceElements[i].getClassName());
                }
            }
        };
    }
    
    public static <T> Assertor<Throwable> hasMethodInStackTraceAt(final int stackTraceIndex, final String method) {
        return new Assertor<Throwable>() {

            private String actualMethodAtIndex;

            public void assertValue(final Throwable throwable) {
                actualMethodAtIndex = throwable.getStackTrace()[stackTraceIndex].getMethodName();
                assertTrue(
                    "Method at index: " + stackTraceIndex + 
                    "\n" +
                    "expected to be: " + method + 
                    "\n" +
                    "but is: " + actualMethodAtIndex,
                    actualMethodAtIndex.equals(method));
            }
        };
    }
    
    public static <T> Assertor<Object> hasBridgeMethod(final String methodName) {
        return new Assertor<Object>() {

            public void assertValue(final Object o) {
                Class clazz = null;
                if (o instanceof Class) {
                    clazz = (Class) o;
                } else {
                    clazz = o.getClass();
                }
                
                for (final Method m : clazz.getMethods()) {
                    if (m.isBridge() && m.getName().equals(methodName)) {
                        return;
                    }
                }
                
                fail("Bridge method [" + methodName + "]\nnot found in:\n" + o);
            }
        };
    }
    
    public static <T> Assertor<Collection> hasExactlyInOrder(final T ... elements) {
        return new Assertor<Collection>() {

            public void assertValue(final Collection value) {
                assertEquals(elements.length, value.size());
                
                final boolean containsSublist = Collections.indexOfSubList((List<?>) value, Arrays.asList(elements)) != -1;
                assertTrue(
                        "Elements:" +
                        "\n" + 
                        Arrays.toString(elements) + 
                        "\n" +
                        "were not found in collection:" +
                        "\n" +
                        value, containsSublist);
            }
        };
    }
    
    public static <T> Assertor<Collection> contains(final Matcher<T> ... elements) {
        return new Assertor<Collection>() {
            
            public void assertValue(final Collection value) {
                int matched = 0;
                for (final Matcher<T> m : elements) {
                    for (final Object el : value) {
                        if (m.matches(el)) {
                            matched++;
                            continue;
                        }
                    }
                }
                
                assertEquals("At least one of the matchers failed to match any of the elements", elements.length, matched);
            }
        };
    }
    
    public static org.hamcrest.Matcher<java.lang.Object> clazz(final java.lang.Class<?> type) {
        return CoreMatchers.is(type);
    }

    public static Assertor hasMethodsInStackTrace(final String ... methods) {
        return new Assertor<Throwable>() {
            public void assertValue(final Throwable value) {
                final StackTraceElement[] trace = value.getStackTrace();
                for (int i = 0; i < methods.length; i++) {
                    assertEquals("Expected methods[" + i + "] to be in the stack trace.", methods[i], trace[i].getMethodName());
                }
            }
        };
    }
}