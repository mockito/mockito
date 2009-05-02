package org.mockito.internal.runners.util;

import org.junit.Test;
import org.junit.runner.Runner;
import org.mockitoutil.TestBase;


public class ClassProviderTest extends TestBase {
    
    @Test
    public void shouldKnowAboutJUnit45() throws Exception {
        //given
        ClassProvider provider = new ClassProvider();
        //then
        assertTrue(provider.hasJUnit45OrHigher());
        //I cannot test the opposite condition :(
    }
    
    @Test
    public void shouldCreateRunnerInstance() throws Throwable {
        //given
        ClassProvider provider = new ClassProvider();
        //when
        Runner runner = provider.newInstance("org.mockito.internal.runners.MockitoJUnit45AndUpRunner", ClassProviderTest.class);
        //then
        assertNotNull(runner);
    }
}