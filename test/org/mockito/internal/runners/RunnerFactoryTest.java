/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.runners.util.RunnerProvider;
import org.mockitoutil.TestBase;

import java.lang.reflect.InvocationTargetException;

public class RunnerFactoryTest extends TestBase {

    static class ClassProviderStub extends RunnerProvider {
        @Override
        public boolean isJUnit45OrHigherAvailable() {
            return super.isJUnit45OrHigherAvailable();
        }
    }

    @Test
    public void shouldCreateRunnerForJUnit44() throws Exception {
        //given
        RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return false;
            }
        };
        RunnerFactory factory = new RunnerFactory(provider);
        
        //when
        RunnerImpl runner = factory.create(RunnerFactoryTest.class);
        
        //then
        assertThat(runner, is(JUnit44RunnerImpl.class));
    }
    
    @Test
    public void shouldCreateRunnerForJUnit45()  throws Exception{
        //given
        RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return true;
            }
        };
        RunnerFactory factory = new RunnerFactory(provider);
        
        //when
        RunnerImpl runner = factory.create(RunnerFactoryTest.class);
        
        //then
        assertThat(runner, is(JUnit45AndHigherRunnerImpl.class));
    }
    
    @Test
    public void
    shouldThrowMeaningfulMockitoExceptionIfNoValidJUnitFound()  throws Exception{
        //given
        RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return false;
            }
            public RunnerImpl newInstance(String runnerClassName, Class<?> constructorParam) throws Exception {
                throw new InitializationError("Where is JUnit, dude?");
            }
        };
        RunnerFactory factory = new RunnerFactory(provider);
        
        try {
            //when
            factory.create(RunnerFactoryTest.class);
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("upgrade your JUnit version", e.getMessage());
        }
    }

    static class NoTestMethods {}

    @Test
    public void shouldSaySomethingMeaningfulWhenNoTestMethods()  throws Exception{
        //given
        RunnerFactory factory = new RunnerFactory(new RunnerProvider());

        //when
        try {
            factory.create(NoTestMethods.class);
            fail();
        }
        //then
        catch (MockitoException e) {
            assertContains("No tests", e.getMessage());
        }
    }

    @Test
    public void shouldForwardInvocationTargetException()  throws Exception{
        //given
        RunnerFactory factory = new RunnerFactory(new RunnerProvider()
        {
            @Override
            public RunnerImpl newInstance(String runnerClassName, Class<?> constructorParam) throws Exception {
                throw new InvocationTargetException(new RuntimeException());
            }
        });

        //when
        try {
            factory.create(this.getClass());
            fail();
        }
        //then
        catch (InvocationTargetException e) {}
    }
}