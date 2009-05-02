package org.mockito.internal.runners;

import java.lang.reflect.Constructor;

import org.junit.runner.Runner;
import org.mockito.exceptions.base.MockitoException;

public class RunnerFactory {

    public Runner create(Class<?> klass) {
        Class<?> runnerClass;
        try {
            runnerClass = Class.forName("org.mockito.internal.runners.MockitoJUnit45AndUpRunner");
        } catch (Throwable t) {
            try {
                runnerClass = Class.forName("org.mockito.internal.runners.MockitoJUnit44RunnerImpl");
            } catch (Throwable t2) {
                throw new MockitoException(
                        "\n" +
                        "MockitoRunner can only be used with JUnit 4.4 or higher.\n" +
                        "You can upgrade your JUnit version or write your own Runner (please consider contributing your runner to the Mockito community).\n" +
                        "Bear in mind that you can still enjoy all features of the framework without using runners (they are completely optional).\n" +
                        "If you get this error despite using JUnit 4.4 or higher then please report this error to the mockito mailing list.\n"
                        , t);
            }
        }
        
        try {
            Constructor<?> constructor = runnerClass.getConstructor(Class.class.getClass());
            return (Runner) constructor.newInstance(klass);
        } catch (Exception e) {
            //TODO: same exception as above
            throw new MockitoException("foo", e);
        }
    }
}