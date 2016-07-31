package org.mockito.internal.junit;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RuleStubbingsReporterTest extends TestBase {

    RuleStubbingsReporter reporter = new RuleStubbingsReporter();
    SimpleMockitoLogger logger = new SimpleMockitoLogger();

    @Test
    public void no_interactions() throws Exception {
        //when
        reporter.printUnusedStubbings(logger);

        //then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void no_stubbings() throws Exception {
        //given
        reporter.stubbingNotFound(new InvocationBuilder().toInvocation());

        //when
        reporter.printUnusedStubbings(logger);

        //then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void no_unused_stubbings() throws Exception {
        //given
        Invocation stubbing = new InvocationBuilder().toInvocation();
        reporter.newStubbing(stubbing);
        reporter.usedStubbing(stubbing, new InvocationBuilder().toInvocation());

        //when
        reporter.printUnusedStubbings(logger);

        //then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void unused_stubbings() throws Exception {
        //given
        reporter.newStubbing(new InvocationBuilder().toInvocation());

        //when
        reporter.printUnusedStubbings(logger);

        //then
        assertEquals(
            "[MockitoHint] See javadoc for MockitoHint class.\n" +
            "[MockitoHint] unused -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)",
                filterLineNo(logger.getLoggedInfo()));
    }
}