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
    private static ClassLoader classLoaderWithoutJUnit4 = excludingClassLoader().withCodeSourceUrlOf(ExceptionFactory.class, junit.framework.ComparisonFailure.class).without("org.junit").build();

    /** loaded by the current classloader */
    private static Class<?> junit4ComparisonFailure;
    private static Class<?> junit4ArgumentsAreDifferent;

    /** loaded by the classloader {@value #classLoaderWithoutJUnit4}, which excludes JUnit 4 classes */
    private static Class<?> junit3ComparisonFailure;
    private static Class<?> junit3ArgumentsAreDifferent;

    /** loaded by the custom classloader {@value #classLoaderWithoutJUnit}, which excludes JUnit classes */
    private static Class<?> nonJunitArgumentsAreDifferent;

    @BeforeClass
    public static void init() throws ClassNotFoundException {
        nonJunitArgumentsAreDifferent = classLoaderWithoutJUnit.loadClass(ArgumentsAreDifferent.class.getName());
        classLoaderWithoutJUnit4.loadClass(ArgumentsAreDifferent.class.getName());
        junit3ComparisonFailure = classLoaderWithoutJUnit4.loadClass(junit.framework.ComparisonFailure.class.getName());
        junit3ArgumentsAreDifferent = classLoaderWithoutJUnit4.loadClass(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class.getName());
        junit4ComparisonFailure = org.junit.ComparisonFailure.class;
        junit4ArgumentsAreDifferent = org.mockito.exceptions.verification.junit4.ArgumentsAreDifferent.class;
    }

    @Test
    public void createArgumentsAreDifferentException_withoutJUnit() throws Exception {
        AssertionError e = invokeFactoryThroughLoader(classLoaderWithoutJUnit);

        assertThat(e).isExactlyInstanceOf(nonJunitArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit3_butNotJUnit4() throws Exception {
        AssertionError e = invokeFactoryThroughLoader(classLoaderWithoutJUnit4);

        assertThat(e).isExactlyInstanceOf(junit3ArgumentsAreDifferent).isInstanceOf(junit3ComparisonFailure);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit4() throws Exception {
        AssertionError e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");

        assertThat(e).isExactlyInstanceOf(junit4ArgumentsAreDifferent).isInstanceOf(junit4ComparisonFailure);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit3_2x() throws Exception {
        AssertionError e;

        e = invokeFactoryThroughLoader(classLoaderWithoutJUnit4);
        assertThat(e).isExactlyInstanceOf(junit3ArgumentsAreDifferent);

        e = invokeFactoryThroughLoader(classLoaderWithoutJUnit4);
        assertThat(e).isExactlyInstanceOf(junit3ArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit4_2x() throws Exception {
        AssertionError e;

        e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");
        assertThat(e).isExactlyInstanceOf(junit4ArgumentsAreDifferent);

        e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");
        assertThat(e).isExactlyInstanceOf(junit4ArgumentsAreDifferent);
    }

    private static AssertionError invokeFactoryThroughLoader(ClassLoader loader) throws Exception {
        Class<?> exceptionFactory = loader.loadClass(ExceptionFactory.class.getName());

        Method m = exceptionFactory.getDeclaredMethod("createArgumentsAreDifferentException", String.class, String.class, String.class);
        return (AssertionError) m.invoke(null, "message", "wanted", "actual");
    }
}
