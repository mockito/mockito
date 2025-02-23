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

    private static final ClassLoader classLoaderWithoutJUnitOrOpenTest =
            excludingClassLoader()
                    .withCodeSourceUrlOf(ExceptionFactory.class)
                    .without("org.junit", "junit", "org.opentest4j")
                    .build();
    private static final ClassLoader currentClassLoader =
            ExceptionFactoryTest.class.getClassLoader();

    /** loaded by the current classloader */
    private static Class<?> opentestComparisonFailure;

    private static Class<?> opentestArgumentsAreDifferent;

    /** loaded by the custom classloader {@value #classLoaderWithoutJUnitOrOpenTest}, which excludes JUnit and OpenTest4J classes */
    private static Class<?> nonJunitArgumentsAreDifferent;

    @BeforeClass
    public static void init() throws ClassNotFoundException {
        nonJunitArgumentsAreDifferent =
                classLoaderWithoutJUnitOrOpenTest.loadClass(ArgumentsAreDifferent.class.getName());
        opentestComparisonFailure = org.opentest4j.AssertionFailedError.class;
        opentestArgumentsAreDifferent =
                org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent.class;
    }

    @Test
    public void createArgumentsAreDifferentException_withoutJUnitOrOpenTest() throws Exception {
        AssertionError e = invokeFactoryThroughLoader(classLoaderWithoutJUnitOrOpenTest);

        assertThat(e).isExactlyInstanceOf(nonJunitArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withOpenTest() throws Exception {
        AssertionError e = invokeFactoryThroughLoader(currentClassLoader);

        assertThat(e)
                .isExactlyInstanceOf(opentestArgumentsAreDifferent)
                .isInstanceOf(opentestComparisonFailure);
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
    public void createArgumentsAreDifferentException_withOpenTest_2x() throws Exception {
        AssertionError e;

        e = invokeFactoryThroughLoader(currentClassLoader);
        assertThat(e).isExactlyInstanceOf(opentestArgumentsAreDifferent);

        e = invokeFactoryThroughLoader(currentClassLoader);
        assertThat(e).isExactlyInstanceOf(opentestArgumentsAreDifferent);
    }

    private static AssertionError invokeFactoryThroughLoader(ClassLoader loader) throws Exception {
        Class<?> exceptionFactory = loader.loadClass(ExceptionFactory.class.getName());

        Method m =
                exceptionFactory.getDeclaredMethod(
                        "createArgumentsAreDifferentException",
                        String.class,
                        String.class,
                        String.class);
        return (AssertionError) m.invoke(null, "message", "wanted", "actual");
    }
}
