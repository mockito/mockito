/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

public class StubbingArgMismatchesTest extends TestBase {

    SimpleMockitoLogger logger = new SimpleMockitoLogger();
    StubbingArgMismatches mismatches = new StubbingArgMismatches();

    @Test
    public void no_op_when_no_mismatches() throws Exception {
        // when
        mismatches.format("MyTest.myTestMethod", logger);

        // then
        assertTrue(logger.isEmpty());
    }

    @Test
    public void logs_mismatch() throws Exception {
        // given
        mismatches.add(
                new InvocationBuilder().args("a").location("-> at A.java").toInvocation(),
                new InvocationBuilder().args("b").location("-> at B.java").toInvocation());

        // when
        mismatches.format("MyTest.myTestMethod", logger);

        // then
        assertEquals(
                "[MockitoHint] MyTest.myTestMethod (see javadoc for MockitoHint):\n"
                        + "[MockitoHint] 1. Unused... -> at B.java\n"
                        + "[MockitoHint]  ...args ok? -> at A.java\n",
                logger.getLoggedInfo());
    }

    @Test
    public void multiple_matching_invocations_per_stub_plus_some_other_invocation()
            throws Exception {
        // given
        Invocation stubbing =
                new InvocationBuilder().args("a").location("-> at A.java").toInvocation();
        mismatches.add(
                new InvocationBuilder().args("x").location("-> at X.java").toInvocation(),
                stubbing);
        mismatches.add(
                new InvocationBuilder().args("y").location("-> at Y.java").toInvocation(),
                stubbing);

        mismatches.add(
                new InvocationBuilder()
                        .method("differentMethod")
                        .args("n")
                        .location("-> at N.java")
                        .toInvocation(),
                new InvocationBuilder()
                        .method("differentMethod")
                        .args("m")
                        .location("-> at M.java")
                        .toInvocation());

        // when
        mismatches.format("MyTest.myTestMethod", logger);

        // then
        assertEquals(
                "[MockitoHint] MyTest.myTestMethod (see javadoc for MockitoHint):\n"
                        + "[MockitoHint] 1. Unused... -> at A.java\n"
                        + "[MockitoHint]  ...args ok? -> at X.java\n"
                        + "[MockitoHint]  ...args ok? -> at Y.java\n"
                        + "[MockitoHint] 2. Unused... -> at M.java\n"
                        + "[MockitoHint]  ...args ok? -> at N.java\n",
                logger.getLoggedInfo());
    }
}
