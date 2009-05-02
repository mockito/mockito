package org.mockito.internal.runners;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.runners.util.RunnerProvider;
import org.mockitoutil.TestBase;

public class RunnerFactoryTest extends TestBase {

    static class ClassProviderStub extends RunnerProvider {
        @Override
        public boolean isJUnit45OrHigherAvailable() {
            return super.isJUnit45OrHigherAvailable();
        }
    }

    @Test
    public void shouldCreateRunnerForJUnit44() {
        //given
        RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return false;
            }
        };
        RunnerFactory factory = new RunnerFactory(provider);
        
        //when
        Runner runner = factory.create(RunnerFactoryTest.class);
        
        //then
        assertThat(runner, is(MockitoJUnit44RunnerImpl.class));
    }
    
    @Test
    public void shouldCreateRunnerForJUnit45() {
        //given
        RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return true;
            }
        };
        RunnerFactory factory = new RunnerFactory(provider);
        
        //when
        Runner runner = factory.create(RunnerFactoryTest.class);
        
        //then
        assertThat(runner, is(MockitoJUnit45AndUpRunner.class));
    }
    
    @Test
    public void shouldThrowMeaningfulMockitoExceptionIfNoValidJUnitFound() {
        //given
        RunnerProvider provider = new RunnerProvider() {
            public boolean isJUnit45OrHigherAvailable() {
                return false;
            }
            public Runner newInstance(String runnerClassName, Class<?> constructorParam) throws Throwable {
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
}
