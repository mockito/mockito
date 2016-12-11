/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners.util;

import org.mockito.internal.runners.RunnerImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RunnerProvider {

    public RunnerImpl newInstance(String runnerClassName, Object ... constructorArgs) throws Exception {
        Constructor<?> constructor;
        try {
            Class<?> runnerClass = Class.forName(runnerClassName);
            constructor = runnerClass.getConstructors()[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        try {
            return (RunnerImpl) constructor.newInstance(constructorArgs);
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);        
        }
    }
}