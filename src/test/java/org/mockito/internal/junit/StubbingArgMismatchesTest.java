package org.mockito.internal.junit;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StubbingArgMismatchesTest extends TestBase {

    SimpleMockitoLogger logger = new SimpleMockitoLogger();
    StubbingArgMismatches mismatches = new StubbingArgMismatches();

    @Test
    public void no_op_when_no_mismatches() throws Exception {
        //when
        mismatches.log(logger);

        //then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void logs_mismatch() throws Exception {
        //given
        mismatches.add(
                new InvocationBuilder().args("a").location("-> at A.java").toInvocation(),
                new InvocationBuilder().args("b").location("-> at B.java").toInvocation());

        //when
        mismatches.log(logger);

        //then
        assertEquals(
                "[MockitoHint] See javadoc for MockitoHint class.\n" +
                "[MockitoHint] 1. unused stub  -> at B.java\n" +
                "[MockitoHint]  - arg mismatch -> at A.java", logger.getLoggedInfo());
    }

    @Test
    public void logs_multiple_mismatches() throws Exception {
        //given
        mismatches.add(
                new InvocationBuilder().args("a").location("-> at A.java").toInvocation(),
                new InvocationBuilder().args("b").location("-> at B.java").toInvocation());

        mismatches.add(
                new InvocationBuilder().method("differentMethod").args("x").location("-> at X.java").toInvocation(),
                new InvocationBuilder().method("differentMethod").args("y").location("-> at Y.java").toInvocation());

        //when
        mismatches.log(logger);

        //then
        assertEquals(
        "[MockitoHint] See javadoc for MockitoHint class.\n" +
        "[MockitoHint] 1. unused stub  -> at B.java\n" +
        "[MockitoHint]  - arg mismatch -> at A.java\n" +
        "[MockitoHint] 2. unused stub  -> at Y.java\n" +
        "[MockitoHint]  - arg mismatch -> at X.java", logger.getLoggedInfo());
    }

    @Test
    public void multiple_matching_invocations_per_unused_stub() throws Exception {
        //given
        Invocation stubbing = new InvocationBuilder().args("a").location("-> at A.java").toInvocation();
        mismatches.add(new InvocationBuilder().args("x").location("-> at X.java").toInvocation(), stubbing);
        mismatches.add(new InvocationBuilder().args("y").location("-> at Y.java").toInvocation(), stubbing);

        //when
        mismatches.log(logger);

        //then
        assertEquals(
        "[MockitoHint] See javadoc for MockitoHint class.\n" +
        "[MockitoHint] 1. unused stub  -> at A.java\n" +
        "[MockitoHint]  - arg mismatch -> at X.java\n" +
        "[MockitoHint]  - arg mismatch -> at Y.java", logger.getLoggedInfo());
    }

    @Test
    public void multiple_matching_invocations_per_stub_plus_some_other_invocation() throws Exception {
        //given
        Invocation stubbing = new InvocationBuilder().args("a").location("-> at A.java").toInvocation();
        mismatches.add(new InvocationBuilder().args("x").location("-> at X.java").toInvocation(), stubbing);
        mismatches.add(new InvocationBuilder().args("y").location("-> at Y.java").toInvocation(), stubbing);

        mismatches.add(
                new InvocationBuilder().method("differentMethod").args("n").location("-> at N.java").toInvocation(),
                new InvocationBuilder().method("differentMethod").args("m").location("-> at M.java").toInvocation());

        //when
        mismatches.log(logger);

        //then
        assertEquals(
        "[MockitoHint] See javadoc for MockitoHint class.\n" +
        "[MockitoHint] 1. unused stub  -> at A.java\n" +
        "[MockitoHint]  - arg mismatch -> at X.java\n" +
        "[MockitoHint]  - arg mismatch -> at Y.java\n" +
        "[MockitoHint] 2. unused stub  -> at M.java\n" +
        "[MockitoHint]  - arg mismatch -> at N.java", logger.getLoggedInfo());
    }
}