package org.mockitousage.junitrule;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class StubbingWarningsMultiThreadingTest extends TestBase {

    private SimpleMockitoLogger logger = new SimpleMockitoLogger();
    private JUnitRule rule = new JUnitRule(logger, false);
    private FrameworkMethod dummy = mock(FrameworkMethod.class);

    @After
    public void after() {
        //so that the validate framework usage exceptions do not collide with the tests here
        resetState();
    }

    @Test public void using_stubbing_from_different_thread() throws Throwable {
        rule.apply(new Statement() {
            public void evaluate() throws Throwable {
                //given: some mock with stubbing
                final IMethods mock = mock(IMethods.class);
                when(mock.simpleMethod()).thenReturn("1");

                //when: use the stubbing from a different thread
                new Thread() {
                    public void run() {
                        mock.simpleMethod();
                    }
                }.join();
            }
        }, dummy, this).evaluate();

        //then: there are no stubbing warnings
        assertTrue(logger.isEmpty());
    }
}
