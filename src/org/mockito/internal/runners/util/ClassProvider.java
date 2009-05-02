package org.mockito.internal.runners.util;

import java.lang.reflect.Constructor;

import org.junit.runner.Runner;

public class ClassProvider {

    private static boolean hasJUnit45OrHigher; 

    static {
        try {
            Class.forName("org.junit.runners.BlockJUnit4ClassRunner");
            hasJUnit45OrHigher = true;
        } catch (Throwable t) {
            hasJUnit45OrHigher = false;
        }
    }
    
    public boolean hasJUnit45OrHigher() {
        return hasJUnit45OrHigher;
    }

    public Runner newInstance(String runnerClassName, Class<?> constructorParam) throws Throwable {
        Class<?> runnerClass = Class.forName(runnerClassName);
        Constructor<?> constructor = runnerClass.getConstructor(Class.class.getClass());
        return (Runner) constructor.newInstance(constructorParam);   
    }
}