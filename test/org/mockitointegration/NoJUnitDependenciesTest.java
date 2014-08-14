package org.mockitointegration;

import java.util.Set;
import org.hamcrest.Matcher;
import org.junit.Assume;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockitoutil.ClassLoaders;
import org.objenesis.Objenesis;
import net.bytebuddy.ByteBuddy;

public class NoJUnitDependenciesTest {
    @Test
    public void pure_mockito_should_not_depend_JUnit___CGLIB() throws Exception {
        Assume.assumeTrue("CglibMockMaker".equals(Plugins.getMockMaker().getClass().getSimpleName()));

        ClassLoader classLoader_without_JUnit = ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(
                        Mockito.class,
                        Matcher.class,
                        Enhancer.class,
                        Objenesis.class
                )
                .without("junit", "org.junit")
                .build();

        Set<String> pureMockitoAPIClasses = ClassLoaders.in(classLoader_without_JUnit).omit("runners", "junit", "JUnit").listOwnedClasses();

        for (String pureMockitoAPIClass : pureMockitoAPIClasses) {
            checkDependency(classLoader_without_JUnit, pureMockitoAPIClass);
        }
    }

    @Test
    public void pure_mockito_should_not_depend_JUnit___ByteBuddy() throws Exception {
        Assume.assumeTrue("ByteBuddyMockMaker".equals(Plugins.getMockMaker().getClass().getSimpleName()));

        ClassLoader classLoader_without_JUnit = ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(
                        Mockito.class,
                        Matcher.class,
                        Enhancer.class,
                        ByteBuddy.class,
                        Objenesis.class
                )
                .without("junit", "org.junit")
                .build();

        Set<String> pureMockitoAPIClasses = ClassLoaders.in(classLoader_without_JUnit).omit("runners", "junit", "JUnit").listOwnedClasses();

        for (String pureMockitoAPIClass : pureMockitoAPIClasses) {
            checkDependency(classLoader_without_JUnit, pureMockitoAPIClass);
        }
    }

    private void checkDependency(ClassLoader classLoader_without_JUnit, String pureMockitoAPIClass) throws ClassNotFoundException {
        try {
            Class.forName(pureMockitoAPIClass, true, classLoader_without_JUnit);
        } catch (Throwable e) {
            throw new AssertionError(String.format("'%s' has some dependency to JUnit", pureMockitoAPIClass), e);
        }
    }
}
