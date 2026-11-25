/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.strictness;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

/**
 * Per-mock strictness configured with {@code MockSettings#strictness(Strictness)} must be observed
 * even when the mock is created outside an active session, rule or extension, e.g. in a field
 * initializer. See issue #3262.
 */
public class StrictnessOutsideSessionTest {

    // the mock is intentionally created before any session, rule or extension is active
    private final IMethods strictMock =
            mock(IMethods.class, withSettings().strictness(Strictness.STRICT_STUBS));

    @Test
    public void strict_mock_created_outside_session_fails_on_stubbing_arg_mismatch() {
        when(strictMock.simpleMethod("1")).thenReturn("1");

        assertThatThrownBy(() -> ProductionCode.simpleMethod(strictMock, "3"))
                .isInstanceOf(PotentialStubbingProblem.class);
    }

    @Test
    public void strict_mock_created_outside_session_honors_lenient_stubbing() {
        lenient().when(strictMock.simpleMethod("1")).thenReturn("1");

        assertThatCode(() -> ProductionCode.simpleMethod(strictMock, "3"))
                .doesNotThrowAnyException();
    }

    @Test
    public void lenient_mock_created_outside_session_does_not_fail_on_arg_mismatch() {
        IMethods lenientMock = mock(IMethods.class, withSettings().strictness(Strictness.LENIENT));
        when(lenientMock.simpleMethod("1")).thenReturn("1");

        assertThatCode(() -> ProductionCode.simpleMethod(lenientMock, "3"))
                .doesNotThrowAnyException();
    }

    @Test
    public void per_mock_strictness_beats_lenient_session_for_mock_created_before_session() {
        MockitoSession session =
                Mockito.mockitoSession().strictness(Strictness.LENIENT).startMocking();
        try {
            when(strictMock.simpleMethod("1")).thenReturn("1");

            assertThatThrownBy(() -> ProductionCode.simpleMethod(strictMock, "3"))
                    .isInstanceOf(PotentialStubbingProblem.class);
        } finally {
            session.finishMocking();
        }
    }

    @Test
    public void realized_stubbing_of_strict_mock_is_implicitly_verified() {
        when(strictMock.simpleMethod("1")).thenReturn("1");
        strictMock.simpleMethod("1");

        // strict stubbing implies that realized stubbings are marked as verified,
        // the same way as when the mock is created during a session
        verifyNoMoreInteractions(strictMock);
    }
}
