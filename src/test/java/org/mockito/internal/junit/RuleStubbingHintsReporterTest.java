package org.mockito.internal.junit;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class RuleStubbingHintsReporterTest extends TestBase {

    RuleStubbingHintsReporter reporter = new RuleStubbingHintsReporter();
    SimpleMockitoLogger logger = new SimpleMockitoLogger();

    @Test
    public void no_interactions() throws Exception {
        //when
        reporter.printUnusedStubbings(logger);
        reporter.printStubbingMismatches(logger);

        //then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void no_stubbings() throws Exception {
        //given
        reporter.stubbingNotFound(new InvocationBuilder().toInvocation());
        reporter.printStubbingMismatches(logger);

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
        reporter.printStubbingMismatches(logger);

        //then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void unused_stubbings() throws Exception {
        //given
        reporter.newStubbing(new InvocationBuilder().toInvocation());
        reporter.newStubbing(new InvocationBuilder().toInvocation());

        //when
        reporter.printUnusedStubbings(logger);

        //then
        assertEquals(
            "[MockitoHint] See javadoc for MockitoHint class.\n" +
            "[MockitoHint] 1. unused -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
            "[MockitoHint] 2. unused -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)",
                filterLineNo(logger.getLoggedInfo()));
    }

    @Test
    public void no_stubbing_mismatch_when_mock_different() throws Exception {
        //given
        reporter.newStubbing(new InvocationBuilder().arg("a").toInvocation());
        reporter.stubbingNotFound(new InvocationBuilder().arg("b").toInvocation()); // <-- different mock

        //when
        reporter.printStubbingMismatches(logger);

        //then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void no_stubbing_mismatch_when_method_different() throws Exception {
        //given
        IMethods mock = mock(IMethods.class);
        reporter.newStubbing(new InvocationBuilder().mock(mock).arg("a").toInvocation());
        reporter.stubbingNotFound(new InvocationBuilder().mock(mock).differentMethod().toInvocation());

        //when
        reporter.printStubbingMismatches(logger);

        //then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void stubbing_mismatch() throws Exception {
        //given
        IMethods mock = mock(IMethods.class);
        reporter.newStubbing(new InvocationBuilder().mock(mock).arg("a").toInvocation());
        reporter.stubbingNotFound(new InvocationBuilder().mock(mock).arg("b").toInvocation());

        //when
        reporter.printStubbingMismatches(logger);

        //then
        assertEquals(
            "[MockitoHint] See javadoc for MockitoHint class.\n" +
            "[MockitoHint] 1. unused stub  -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
            "[MockitoHint]  - arg mismatch -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)", logger.getLoggedInfo());
    }
}