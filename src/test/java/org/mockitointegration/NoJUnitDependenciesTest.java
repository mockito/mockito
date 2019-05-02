/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitointegration;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.hamcrest.Matcher;
import org.junit.Assume;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockitoutil.ClassLoaders;
import org.objenesis.Objenesis;

import java.util.Set;

import static org.mockitoutil.ClassLoaders.coverageTool;

public class NoJUnitDependenciesTest {

    @Test
    public void pure_mockito_should_not_depend_JUnit___ByteBuddy() throws Exception {
        Assume.assumeTrue("ByteBuddyMockMaker".equals(Plugins.getMockMaker().getClass().getSimpleName()));

        ClassLoader classLoader_without_JUnit = ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(
                        Mockito.class,
                        Matcher.class,
                        ByteBuddy.class,
                        ByteBuddyAgent.class,
                        Objenesis.class
                )
                .withCodeSourceUrlOf(coverageTool())
                .without("junit", "org.junit", "org.opentest4j")
                .build();

        Set<String> pureMockitoAPIClasses = ClassLoaders.in(classLoader_without_JUnit).omit("runners", "junit", "JUnit", "opentest4j").listOwnedClasses();

        // The later class is required to be initialized before any inline mock maker classes can be loaded.
        checkDependency(classLoader_without_JUnit, "org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker");
        pureMockitoAPIClasses.remove("org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker");

        for (String pureMockitoAPIClass : pureMockitoAPIClasses) {
            checkDependency(classLoader_without_JUnit, pureMockitoAPIClass);
        }
    }

    private void checkDependency(ClassLoader classLoader_without_JUnit, String pureMockitoAPIClass) throws ClassNotFoundException {
        try {
            Class.forName(pureMockitoAPIClass, true, classLoader_without_JUnit);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new AssertionError(String.format("'%s' has some dependency to JUnit", pureMockitoAPIClass));
        }
    }
}
