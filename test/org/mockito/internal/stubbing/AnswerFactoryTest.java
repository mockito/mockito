/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.base.StackTraceFilter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import static org.mockito.util.ExtraMatchers.hasFirstMethodInStackTrace;

@SuppressWarnings("unchecked")
public class AnswerFactoryTest extends TestBase {

    private StackTraceFilterStub filterStub;
    private AnswerFactory factory;

    @Before
    public void setup() {
        this.filterStub = new StackTraceFilterStub();
        this.factory = new AnswerFactory(filterStub);
    }

    @Test
    public void shouldCreateReturnResult() throws Throwable {
        Answer result = factory.createReturningAnswer("lol");
        assertEquals("lol", result.answer(null));
    }

    @Test(expected = RuntimeException.class)
    public void shouldCreateThrowResult() throws Throwable {
        factory.createThrowingAnswer(new RuntimeException(), null).answer(null);
    }

    @Test
    public void shouldFilterStackTraceWhenCreatingThrowResult() throws Throwable {
        Answer result = factory.createThrowingAnswer(new RuntimeException(), null);
        try {
            result.answer(null);
            fail();
        } catch (RuntimeException e) {
            assertTrue(Arrays.equals(filterStub.hasStackTrace.getStackTrace(), e.getStackTrace()));
            assertThat(e, hasFirstMethodInStackTrace("answer"));
        }
    }

    @Test
    public void shouldValidateNullThrowable() throws Throwable {
        try {
            factory.createThrowingAnswer(null, null);
            fail();
        } catch (MockitoException e) {
        }
    }

    @Test
    public void shouldAllowSettingProperCheckedException() throws Throwable {
        Invocation invocation = new InvocationBuilder().method("canThrowException").toInvocation();
        factory.createThrowingAnswer(new CharacterCodingException(), invocation);
    }

    @Test(expected = MockitoException.class)
    public void shouldValidateCheckedException() throws Throwable {
        Invocation invocation = new InvocationBuilder().method("canThrowException").toInvocation();
        factory.createThrowingAnswer(new IOException(), invocation);
    }

    class StackTraceFilterStub extends StackTraceFilter {
        HasStackTrace hasStackTrace;

        @Override
        public void filterStackTrace(HasStackTrace hasStackTrace) {
            this.hasStackTrace = hasStackTrace;
        }
    }
}
