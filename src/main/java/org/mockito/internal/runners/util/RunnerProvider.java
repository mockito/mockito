/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.mockito.internal.runners.RunnerImpl;

public class RunnerProvider {

    private static boolean hasJUnit45OrHigher; 

    static {
        try {
            Class.forName("org.junit.runners.BlockJUnit4ClassRunner");
            hasJUnit45OrHigher = true;
        } catch (Throwable t) {
            hasJUnit45OrHigher = false;
        }
    }
    
    public boolean isJUnit45OrHigherAvailable() {
        return hasJUnit45OrHigher;
    }

    public RunnerImpl newInstance(String runnerClassName, Class<?> constructorParam) throws Exception {
        Constructor<?> constructor;
        try {
            Class<?> runnerClass = Class.forName(runnerClassName);
            constructor = runnerClass.getConstructor(Class.class.getClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        try {
            return (RunnerImpl) constructor.newInstance(constructorParam);
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);        
        }
    }
}