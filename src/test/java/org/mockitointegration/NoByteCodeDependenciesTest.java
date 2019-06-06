/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitointegration;

import org.hamcrest.Matcher;
import org.junit.*;
import org.mockito.Mockito;
import org.mockitoutil.ClassLoaders;

import java.util.Set;

import static org.mockitoutil.ClassLoaders.coverageTool;

public class NoByteCodeDependenciesTest {

    private ClassLoader contextClassLoader;

    @Test
    public void pure_mockito_should_not_depend_bytecode_libraries() throws Exception {

        ClassLoader classLoader_without_bytecode_libraries = ClassLoaders.excludingClassLoader()
            .withCodeSourceUrlOf(
                Mockito.class,
                Matcher.class
            )
            .withCodeSourceUrlOf(coverageTool())
            .without("net.bytebuddy", "org.objenesis")
            .build();

        Set<String> pureMockitoAPIClasses = ClassLoaders.in(classLoader_without_bytecode_libraries).omit(
            "bytebuddy", "runners", "junit", "JUnit", "opentest4j").listOwnedClasses();
        pureMockitoAPIClasses.remove("org.mockito.internal.creation.instance.DefaultInstantiatorProvider");
        pureMockitoAPIClasses.remove("org.mockito.internal.creation.instance.ObjenesisInstantiator");

        // Remove classes that trigger plugin-loading, since bytebuddy plugins are the default.
        pureMockitoAPIClasses.remove("org.mockito.internal.debugging.LocationImpl");
        pureMockitoAPIClasses.remove("org.mockito.internal.exceptions.stacktrace.StackTraceFilter");
        pureMockitoAPIClasses.remove("org.mockito.internal.util.MockUtil");

        for (String pureMockitoAPIClass : pureMockitoAPIClasses) {
            checkDependency(classLoader_without_bytecode_libraries, pureMockitoAPIClass);
        }
    }

    private void checkDependency(ClassLoader classLoader, String pureMockitoAPIClass) throws ClassNotFoundException {
        try {
            Class.forName(pureMockitoAPIClass, true, classLoader);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new AssertionError(String.format("'%s' has some dependency to Byte Buddy or Objenesis", pureMockitoAPIClass));
        }
    }
}
