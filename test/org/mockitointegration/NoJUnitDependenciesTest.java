package org.mockitointegration;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.junit.MockitoJUnit;
import org.mockitoutil.ClassLoaders;
import org.objenesis.Objenesis;

import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class NoJUnitDependenciesTest {

    @Test
    public void pure_mockito_should_not_depend_JUnit() throws Throwable {
        ClassLoader classLoader_without_JUnit = ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(
                        Mockito.class,
                        Matcher.class,
                        Enhancer.class,
                        Objenesis.class,
                        hackToPossiblyIncludeCobertura()
                )
                .without("junit", "org.junit")
                .build();

        Set<String> pureMockitoAPIClasses = ClassLoaders.in(classLoader_without_JUnit)
                .omit("runners", "junit", "JUnit")
                .listOwnedClasses();

        for (String pureMockitoAPIClass : pureMockitoAPIClasses) {
            checkDependency(classLoader_without_JUnit, pureMockitoAPIClass);
        }
    }

    @Test
    public void verify_failure() throws Throwable {
        ClassLoader classLoader_without_JUnit = ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(
                        Mockito.class,
                        Matcher.class,
                        Enhancer.class,
                        Objenesis.class,
                        hackToPossiblyIncludeCobertura()
                )
                .without("junit", "org.junit")
                .build();

        try {
            checkDependency(classLoader_without_JUnit, MockitoJUnit.class.getName());
        } catch (AssertionError e) {
            assertThat(e.getCause()).isInstanceOf(NoClassDefFoundError.class);
            assertThat(e.getCause().getMessage()).contains("junit");
        }
    }

    private void checkDependency(ClassLoader classLoader_without_JUnit, String pureMockitoAPIClass) throws Throwable {
        try {
            Class.forName(pureMockitoAPIClass, true, classLoader_without_JUnit).getDeclaredFields();
        } catch (Throwable e) {
            if(e instanceof NoClassDefFoundError) {
                throw new AssertionError(String.format("'%s' has some unwanted class dependency", pureMockitoAPIClass), e);
            }
            throw e;
        }
    }

    private Class<?> hackToPossiblyIncludeCobertura() throws ClassNotFoundException {
        try {
            return Class.forName("net.sourceforge.cobertura.coveragedata.TouchCollector");
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }
}
