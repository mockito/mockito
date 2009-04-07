/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class ExtraMatchers {

    //TODO remove
    public static <T> Assertor<Throwable> hasFirstMethodInStackTrace(final String method) {
        return hasMethodInStackTraceAt(0, method);
    }
    
    public static <T> Assertor<Throwable> hasOnlyThoseClassesInStackTrace(final String ... classes) {
        return new Assertor<Throwable>() {
            public void assertValue(Throwable traceElements) {
                StackTraceElement[] trace = traceElements.getStackTrace();
                
                assertEquals("Number of classes does not match",
                        classes.length, trace.length);
                    
                for (int i = 0; i < trace.length; i++) {
                    assertEquals(classes[i], trace[i].getClassName());
                }
            }
        };
    }
    
    public static <T> Assertor<Throwable> hasMethodInStackTraceAt(final int stackTraceIndex, final String method) {
        return new Assertor<Throwable>() {

            private String actualMethodAtIndex;

            public void assertValue(Throwable throwable) {
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

            public void assertValue(Object o) {
                Class clazz = null;
                if (o instanceof Class) {
                    clazz = (Class) o;
                } else {
                    clazz = o.getClass();
                }
                
                for (Method m : clazz.getMethods()) {
                    if (m.isBridge() && m.getName().equals(methodName)) {
                        return;
                    }
                }
                
                fail("Bridge method [" + methodName + "]\nnot found in:\n" + o);
            }
        };
    }
    
    public static <T> Assertor<Collection> has(final T ... elements) {
        return new Assertor<Collection>() {

            public void assertValue(Collection value) {
                for (T element : elements) {
                    assertTrue(
                            "Element:" +
                            "\n" +
                            element +
                            "does not exists in:" +
                            "\n" +
                            value, 
                            value.contains(element));
                }
            }
        };
    }
    
    public static <T> Assertor<Collection> hasExactlyInOrder(final T ... elements) {
        return new Assertor<Collection>() {

            public void assertValue(Collection value) {
                assertEquals(elements.length, value.size());
                
                boolean containsSublist = Collections.indexOfSubList((List<?>) value, Arrays.asList(elements)) != -1;
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
}