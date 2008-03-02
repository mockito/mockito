/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

@SuppressWarnings("unchecked")
public class ExtraMatchers extends CoreMatchers {

    public static <T> Matcher<Throwable> hasFirstMethodInStackTrace(final String method) {
        return hasMethodInStackTraceAt(0, method);
    }
    
    public static <T> Matcher<Throwable> hasMethodInStackTraceAt(final int stackTraceIndex, final String method) {
        return new BaseMatcher<Throwable>() {

            private String actualMethodAtIndex;

            public boolean matches(Object throwable) {
                actualMethodAtIndex = ((Throwable) throwable).getStackTrace()[stackTraceIndex].getMethodName();
                return  actualMethodAtIndex.equals(method);
            }

            public void describeTo(Description desc) {
                desc.appendText("Method of index: " + stackTraceIndex + " expected to be: " + method + " but is: " + actualMethodAtIndex);
            }
        };
    }
    
    public static <T> Matcher<Throwable> messageContains(final String text) {
        return new BaseMatcher<Throwable>() {
            public boolean matches(Object throwable) {
                return ((Throwable) throwable).getMessage().contains(text);
            }
            public void describeTo(Description desc) {
                desc.appendText("exception's message doesn't contain " + text);
            }
        };
    }
    
    public static <T> Matcher<Throwable> causeMessageContains(final String text) {
        return new BaseMatcher<Throwable>() {
            public boolean matches(Object throwable) {
                Throwable cause = ((Throwable) throwable).getCause();
                return cause == null ? false : cause.getMessage().contains(text);
            }
            public void describeTo(Description desc) {
                desc.appendText("exception cause's message is not " + text);
            }
        };
    }
    
    public static <T> Matcher<Object> hasBridgeMethod(final String methodName) {
        return new BaseMatcher<Object>() {

            public boolean matches(Object o) {
                Class clazz = null;
                if (o instanceof Class) {
                    clazz = (Class) o;
                } else {
                    clazz = o.getClass();
                }
                
                for (Method m : clazz.getMethods()) {
                    if (m.isBridge()) {
                        if (m.getName().equals(methodName)) {
                            return true;
                        }
                    }
                }
                
                return false; 
            }

            public void describeTo(Description desc) {
                desc.appendText("Bridge method: " + methodName + " not found!");
            }
        };
    }
    
    //TODO can't you use matchers from hamcrest?
    public static <T> Matcher<Collection> collectionHas(final T ... elements) {
        return new BaseMatcher<Collection>() {

            public boolean matches(Object collection) {
                for (T element : elements) {
                    if (((Collection) collection).contains(element) == false) {
                        return false;
                    }
                }
                return true;
            }

            public void describeTo(Description desc) {
                desc.appendText("collection doesn't containg one of: " + Arrays.toString(elements));
            }
        };
    }
    
    //TODO can't you use matchers from hamcrest?
    public static <T> Matcher<Collection> collectionHasExactlyInOrder(final T ... elements) {
        return new BaseMatcher<Collection>() {

            public boolean matches(Object collection) {
                Collection actual = (Collection) collection;
                if (actual.size() != elements.length) {
                    return false;
                }
                if (Collections.indexOfSubList((List<?>) actual, Arrays.asList(elements)) == -1) {
                    return false;
                }
                
                return true;
            }

            public void describeTo(Description desc) {
                desc.appendText("collection doesn't contain following elements in order: " + Arrays.toString(elements));
            }
        };
    }
}
