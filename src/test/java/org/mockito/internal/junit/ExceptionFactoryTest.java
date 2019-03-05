/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockitoutil.ClassLoaders.excludingClassLoader;

import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;

public class ExceptionFactoryTest {

    private static ClassLoader classLoaderWithoutJUnit  = excludingClassLoader().withCodeSourceUrlOf(ExceptionFactory.class).without("org.junit", "junit").build();
    
    /** loaded by the current classloader */
    private static Class<?> comparisonFailure;
    private static Class<?> junitArgumentsAreDifferent;

    /** loaded by the custom classloader {@value #classLoaderWithoutJUnit}, which excludes JUnit classes */
    private static Class<?> nonJunitArgumentsAreDifferent;

    @BeforeClass
    public static void init() throws ClassNotFoundException {
        nonJunitArgumentsAreDifferent = classLoaderWithoutJUnit.loadClass(ArgumentsAreDifferent.class.getName());
        comparisonFailure = junit.framework.ComparisonFailure.class;
        junitArgumentsAreDifferent = org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class;
    }

    @Test
    public void createArgumentsAreDifferentException_withoutJUnit() throws Exception {
        AssertionError e = invokeFactoryThroughLoader(classLoaderWithoutJUnit);

        assertThat(e).isExactlyInstanceOf(nonJunitArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit() throws Exception {
        AssertionError e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");

        assertThat(e).isExactlyInstanceOf(junitArgumentsAreDifferent).isInstanceOf(comparisonFailure);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit_2x() throws Exception {
        AssertionError e;
        
        e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");
        assertThat(e).isExactlyInstanceOf(junitArgumentsAreDifferent);

        e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");
        assertThat(e).isExactlyInstanceOf(junitArgumentsAreDifferent);
    }

    private static AssertionError invokeFactoryThroughLoader(ClassLoader loader) throws Exception {
        Class<?> exceptionFactory = loader.loadClass(ExceptionFactory.class.getName());

        Method m = exceptionFactory.getDeclaredMethod("createArgumentsAreDifferentException", String.class, String.class, String.class);
        return (AssertionError) m.invoke(null, "message", "wanted", "actual");
    }
}
