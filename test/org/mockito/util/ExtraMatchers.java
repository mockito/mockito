/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.util;

import java.lang.reflect.Method;
import java.util.*;

import org.hamcrest.*;

@SuppressWarnings("unchecked")
public class ExtraMatchers {

    public static <T> Matcher<Throwable> firstMethodOnStackEqualsTo(final String method) {
        return new BaseMatcher<Throwable>() {

            private String firstMethodOnStack;

            public boolean matches(Object throwable) {
                firstMethodOnStack = ((Throwable) throwable).getStackTrace()[0].getMethodName();
                return  firstMethodOnStack.equals(method);
            }

            public void describeTo(Description desc) {
                desc.appendText("first method expected to be: " + method + " but is: " + firstMethodOnStack);
            }
        };
    }
    
    public static <T> Matcher<Collection> collectionContaining(final T ... elements) {
        return new BaseMatcher<Collection>() {

            public boolean matches(Object collection) {
                for (T element : elements) {
                    if (((Collection)collection).contains(element) == false) {
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
    
    public static <T> Matcher<String> matches(final String regexp) {
        return new BaseMatcher<String>() {

            public boolean matches(Object string) {
                return ((String)string).matches(regexp); 
            }

            public void describeTo(Description desc) {
                desc.appendText("string doesn't match " + regexp);
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
                            System.out.println(m);
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
}
