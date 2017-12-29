/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.stubbing.Stubbing;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.stubbing.answers.DoesNothing.doesNothing;

public class UnusedStubbingsTest extends TestBase {

    private SimpleMockitoLogger logger = new SimpleMockitoLogger();

    @Test
    public void no_unused_stubbings() throws Exception {
        //given
        UnusedStubbings stubbings = new UnusedStubbings(Collections.<Stubbing>emptyList());

        //when
        stubbings.format("MyTest.myTestMethod", logger);

        //then
        assertEquals("", logger.getLoggedInfo());
    }

    @Test
    public void unused_stubbings() throws Exception {
        //given
        UnusedStubbings stubbings = new UnusedStubbings(Arrays.asList(
            new StubbedInvocationMatcher(doesNothing(), new InvocationBuilder().toInvocationMatcher(), null),
            new StubbedInvocationMatcher(doesNothing(), new InvocationBuilder().toInvocationMatcher(), null)
        ));


        //when
        stubbings.format("MyTest.myTestMethod", logger);

        //then
        assertThat(filterLineNo(logger.getLoggedInfo())).isIn(
            "[MockitoHint] MyTest.myTestMethod (see javadoc for MockitoHint):\n" +  //Java <9
                                    "[MockitoHint] 1. Unused -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                                    "[MockitoHint] 2. Unused -> at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n",
            "[MockitoHint] MyTest.myTestMethod (see javadoc for MockitoHint):\n" +  //Java 9
                                    "[MockitoHint] 1. Unused -> at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                                    "[MockitoHint] 2. Unused -> at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n"
        );
    }
}
