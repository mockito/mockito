/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked"})
public class MockHandlerImplTest extends TestBase {

    @Test
    public void should_remove_verification_mode_even_when_invalid_matchers() throws Throwable {
        // given
        Invocation invocation = new InvocationBuilder().toInvocation();
        @SuppressWarnings("rawtypes")
        MockHandlerImpl<?> handler = new MockHandlerImpl(new MockSettingsImpl());
        mockingProgress().verificationStarted(VerificationModeFactory.atLeastOnce());
        handler.matchersBinder =
                new MatchersBinder() {
                    public InvocationMatcher bindMatchers(
                            ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
                        throw new InvalidUseOfMatchersException();
                    }
                };

        try {
            // when
            handler.handle(invocation);

            // then
            fail();
        } catch (InvalidUseOfMatchersException ignored) {
        }

        assertNull(mockingProgress().pullVerificationMode());
    }

    @Test
    public void should_throw_mockito_exception_when_invocation_handler_throws_anything() {
        // given
        InvocationListener throwingListener = mock(InvocationListener.class);
        // when / then
        assertThatThrownBy(
                        () -> {
                            doThrow(new Throwable())
                                    .when(throwingListener)
                                    .reportInvocation(any(MethodInvocationReport.class));
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Checked exception is invalid for this method!",
                        "Invalid: java.lang.Throwable");
    }

    @Test
    public void should_report_bogus_default_answer() throws Throwable {
        // given
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandlerImpl<?> handler = new MockHandlerImpl(mockSettings);
        given(mockSettings.getDefaultAnswer()).willReturn(new Returns(AWrongType.WRONG_TYPE));
        Invocation invocation =
                new InvocationBuilder()
                        .method(Object.class.getDeclaredMethod("toString"))
                        .toInvocation();
        // when / then
        assertThatThrownBy(
                        () -> {
                            @SuppressWarnings("unused") // otherwise cast is not done
                            String there_should_not_be_a_CCE_here =
                                    (String) handler.handle(invocation);
                        })
                .isInstanceOf(WrongTypeOfReturnValue.class)
                .hasMessageContainingAll(
                        "Default answer returned a result with the wrong type:",
                        "AWrongType cannot be returned by toString()",
                        "toString() should return String",
                        "The default answer of iMethods that was configured on the mock is probably incorrectly implemented.");
    }

    private static class AWrongType {
        public static final AWrongType WRONG_TYPE = new AWrongType();
    }
}
