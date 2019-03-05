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

    private static ClassLoader classLoaderWithoutJUnitOrOpenTest = excludingClassLoader().withCodeSourceUrlOf(ExceptionFactory.class).without("org.junit", "junit", "org.opentest4j").build();
    private static ClassLoader classLoaderWithoutOpenTest = excludingClassLoader().withCodeSourceUrlOf(ExceptionFactory.class, org.junit.ComparisonFailure.class).without("org.opentest4j").build();
    private static ClassLoader currentClassLoader = ExceptionFactoryTest.class.getClassLoader();

    /** loaded by the current classloader */
    private static Class<?> opentestComparisonFailure;
    private static Class<?> opentestArgumentsAreDifferent;

    /** loaded by the classloader {@value #classLoaderWithoutOpenTest}, which excludes OpenTest4J classes */
    private static Class<?> junit3ComparisonFailure;
    private static Class<?> junit3ArgumentsAreDifferent;

    /** loaded by the custom classloader {@value #classLoaderWithoutJUnitOrOpenTest}, which excludes JUnit and OpenTest4J classes */
    private static Class<?> nonJunitArgumentsAreDifferent;

    @BeforeClass
    public static void init() throws ClassNotFoundException {
        nonJunitArgumentsAreDifferent = classLoaderWithoutJUnitOrOpenTest.loadClass(ArgumentsAreDifferent.class.getName());
        junit3ComparisonFailure = classLoaderWithoutOpenTest.loadClass(junit.framework.ComparisonFailure.class.getName());
        junit3ArgumentsAreDifferent = classLoaderWithoutOpenTest.loadClass(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class.getName());
        opentestComparisonFailure = org.opentest4j.AssertionFailedError.class;
        opentestArgumentsAreDifferent = org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent.class;
    }

    @Test
    public void createArgumentsAreDifferentException_withoutJUnitOrOpenTest() throws Exception {
        AssertionError e = invokeFactoryThroughLoader(classLoaderWithoutJUnitOrOpenTest);

        assertThat(e).isExactlyInstanceOf(nonJunitArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit3_butNotOpenTest() throws Exception {
        AssertionError e = invokeFactoryThroughLoader(classLoaderWithoutOpenTest);

        assertThat(e).isExactlyInstanceOf(junit3ArgumentsAreDifferent).isInstanceOf(junit3ComparisonFailure);
    }

    @Test
    public void createArgumentsAreDifferentException_withOpenTest() throws Exception {
        AssertionError e = invokeFactoryThroughLoader(currentClassLoader);

        assertThat(e).isExactlyInstanceOf(opentestArgumentsAreDifferent).isInstanceOf(opentestComparisonFailure);
    }

    @Test
    public void createArgumentsAreDifferentException_withoutJUnitOrOpenTest_2x() throws Exception {
        AssertionError e;

        e = invokeFactoryThroughLoader(classLoaderWithoutJUnitOrOpenTest);
        assertThat(e).isExactlyInstanceOf(nonJunitArgumentsAreDifferent);

        e = invokeFactoryThroughLoader(classLoaderWithoutJUnitOrOpenTest);
        assertThat(e).isExactlyInstanceOf(nonJunitArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit3_2x() throws Exception {
        AssertionError e;

        e = invokeFactoryThroughLoader(classLoaderWithoutOpenTest);
        assertThat(e).isExactlyInstanceOf(junit3ArgumentsAreDifferent);

        e = invokeFactoryThroughLoader(classLoaderWithoutOpenTest);
        assertThat(e).isExactlyInstanceOf(junit3ArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withOpenTest_2x() throws Exception {
        AssertionError e;

        e = invokeFactoryThroughLoader(currentClassLoader);
        assertThat(e).isExactlyInstanceOf(opentestArgumentsAreDifferent);

        e = invokeFactoryThroughLoader(currentClassLoader);
        assertThat(e).isExactlyInstanceOf(opentestArgumentsAreDifferent);
    }

    private static AssertionError invokeFactoryThroughLoader(ClassLoader loader) throws Exception {
        Class<?> exceptionFactory = loader.loadClass(ExceptionFactory.class.getName());

        Method m = exceptionFactory.getDeclaredMethod("createArgumentsAreDifferentException", String.class, String.class, String.class);
        return (AssertionError) m.invoke(null, "message", "wanted", "actual");
    }
}
