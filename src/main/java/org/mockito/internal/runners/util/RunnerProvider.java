/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners.util;

import org.mockito.internal.runners.InternalRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RunnerProvider {

    public InternalRunner newInstance(String runnerClassName, Object ... constructorArgs) throws Exception {
        Constructor<?> constructor;
        try {
            Class<?> runnerClass = Class.forName(runnerClassName);
            if (runnerClass.getConstructors().length != 1) {
                throw new IllegalArgumentException("Expected " + runnerClassName + " to have exactly one constructor.");
            }
            constructor = runnerClass.getConstructors()[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            return (InternalRunner) constructor.newInstance(constructorArgs);
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
