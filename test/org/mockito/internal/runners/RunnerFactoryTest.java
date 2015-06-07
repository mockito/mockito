/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import static org.hamcrest.CoreMatchers.is;

import java.lang.reflect.InvocationTargetException;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.InitializationError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.runners.util.RunnerProvider;
import org.mockitoutil.TestBase;

public class RunnerFactoryTest extends TestBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    static class ClassProviderStub extends RunnerProvider {
        @Override
        public boolean isJUnit45OrHigherAvailable() {
            return super.isJUnit45OrHigherAvailable();
        }
    }

    @Test
    public void shouldCreateRunnerForJUnit44() throws Exception {
        // given
        final RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return false;
            }
        };
        final RunnerFactory factory = new RunnerFactory(provider);

        // when
        final RunnerImpl runner = factory.create(RunnerFactoryTest.class);

        // then
        assertThat(runner, is(JUnit44RunnerImpl.class));
    }

    @Test
    public void shouldCreateRunnerForJUnit45() throws Exception {
        // given
        final RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return true;
            }
        };
        final RunnerFactory factory = new RunnerFactory(provider);

        // when
        final RunnerImpl runner = factory.create(RunnerFactoryTest.class);

        // then
        assertThat(runner, is(JUnit45AndHigherRunnerImpl.class));
    }

    @Test
    public void shouldThrowMeaningfulMockitoExceptionIfNoValidJUnitFound() throws Exception {
        // given
        final RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return false;
            }

            public RunnerImpl newInstance(final String runnerClassName, final Class<?> constructorParam) throws Exception {
                throw new InitializationError("Where is JUnit, dude?");
            }
        };
        final RunnerFactory factory = new RunnerFactory(provider);

        // then
        thrown.expect(MockitoException.class);
        thrown.expectMessage(new BaseMatcher<String>() {

            public boolean matches(final Object arg0) {
                return ((String) arg0).contains("upgrade your JUnit version");
            }

            public void describeTo(final Description arg0) {
            }
        });

        // when
        factory.create(RunnerFactoryTest.class);
    }

    static class NoTestMethods {
    }

    @Test
    public void shouldSaySomethingMeaningfulWhenNoTestMethods() throws Exception {
        // given
        final RunnerFactory factory = new RunnerFactory(new RunnerProvider());

        thrown.expect(MockitoException.class);
        thrown.expectMessage(new BaseMatcher<String>() {

            public boolean matches(final Object arg0) {
                return ((String) arg0).contains("No tests");
            }

            public void describeTo(final Description arg0) {
            }
        });

        // when
        factory.create(NoTestMethods.class);
    }

    @Test
    public void shouldForwardInvocationTargetException() throws Exception {
        // given
        final RunnerFactory factory = new RunnerFactory(new RunnerProvider()
        {
            @Override
            public RunnerImpl newInstance(final String runnerClassName, final Class<?> constructorParam) throws Exception {
                throw new InvocationTargetException(new RuntimeException());
            }
        });

        // then
        thrown.expect(InvocationTargetException.class);

        // when
        factory.create(this.getClass());
    }
}