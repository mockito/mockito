/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockitoutil.TestBase;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.stubbing.answers.DoesNothing.doesNothing;

public class UnusedStubbingsTest extends TestBase {

    SimpleMockitoLogger logger = new SimpleMockitoLogger();

    @Test
    public void no_unused_stubbings() throws Exception {
        //given
        UnusedStubbings stubbings = new UnusedStubbings((List) asList());

        //when
        stubbings.format("MyTest.myTestMethod", logger);

        //then
        assertEquals("", logger.getLoggedInfo());
    }

    @Test
    public void unused_stubbings() throws Exception {
        //given
        UnusedStubbings stubbings = new UnusedStubbings((List) asList(
            new StubbedInvocationMatcher(new InvocationBuilder().toInvocationMatcher(), doesNothing()),
            new StubbedInvocationMatcher(new InvocationBuilder().toInvocationMatcher(), doesNothing())
        ));


        //when
        stubbings.format("MyTest.myTestMethod", logger);

        //then
        assertEquals(
                "[MockitoHint] MyTest.myTestMethod (see javadoc for MockitoHint):\n" +
                        "[MockitoHint] 1. Unused -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                        "[MockitoHint] 2. Unused -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n",
                filterLineNo(logger.getLoggedInfo()));
    }
}
