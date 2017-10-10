/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.mockito.internal.framework.DefaultMockitoSession;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockitoutil.TestBase.filterLineNo;

public class StubbingWarningsTest {

    @Mock IMethods mock;

    SimpleMockitoLogger logger = new SimpleMockitoLogger();
    MockitoSession mockito = new DefaultMockitoSession(this, Strictness.WARN, logger);

    @Test public void few_interactions() throws Throwable {
        //when
        mock.simpleMethod(100);
        mock.otherMethod();

        //expect no exception
        mockito.finishMocking();
        logger.assertEmpty();
    }

    @Test public void stubbing_used() throws Throwable {
        //when
        given(mock.simpleMethod(100)).willReturn("100");
        mock.simpleMethod(100);

        //then
        mockito.finishMocking();
        logger.assertEmpty();
    }

    @Test public void unused_stubbed_is_not_implicitly_verified() throws Throwable {
        //when
        given(mock.simpleMethod(100)).willReturn("100");
        mock.simpleMethod(100); // <- stubbing is used
        mock.simpleMethod(200); // <- other method should not generate arg mismatch

        //then
        mockito.finishMocking();
        logger.assertEmpty();
    }

    @Test public void stubbing_argument_mismatch() throws Throwable {
        //when
        given(mock.simpleMethod(100)).willReturn("100");
        mock.simpleMethod(200);

        mockito.finishMocking();

        //TODO - currently we warn about "Unused" instead of "Arg mismatch" below
        //because it was simpler to implement. This can be improved given we put priority to improve the warnings.
        //then
        assertEquals(filterLineNo(
            "[MockitoHint] StubbingWarningsTest.null (see javadoc for MockitoHint):\n" +
            "[MockitoHint] 1. Unused -> at org.mockitousage.stubbing.StubbingWarningsTest.stubbing_argument_mismatch(StubbingWarningsTest.java:0)\n"),
                filterLineNo(logger.getLoggedInfo()));
    }

    @Test public void unused_stubbing() throws Throwable {
        //when
        given(mock.simpleMethod(100)).willReturn("100");

        mockito.finishMocking();

        //then
        assertEquals(filterLineNo(
            "[MockitoHint] StubbingWarningsTest.null (see javadoc for MockitoHint):\n" +
            "[MockitoHint] 1. Unused -> at org.mockitousage.stubbing.StubbingWarningsTest.unused_stubbing(StubbingWarningsTest.java:0)\n"),
                filterLineNo(logger.getLoggedInfo()));
    }
}
