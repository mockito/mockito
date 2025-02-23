/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.Test;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.stubbing.Answer;

public class ClassCastExOnVerifyZeroInteractionsTest {
    public interface TestMock {
        boolean m1();
    }

    @Test
    public void should_not_throw_ClassCastException_when_mock_verification_fails() {
        TestMock test = mock(TestMock.class, (Answer<Object>) invocation -> false);
        test.m1();

        assertThatThrownBy(
                        () -> {
                            verifyNoInteractions(test);
                        })
                .isInstanceOf(NoInteractionsWanted.class)
                .hasMessageContainingAll(
                        "No interactions wanted here:",
                        "But found these interactions on mock 'testMock':",
                        "Actually, above is the only interaction with this mock.");
    }

    @Test
    public void should_report_bogus_default_answer() {
        TestMock test = mock(TestMock.class, (Answer<Object>) invocation -> false);

        assertThatThrownBy(
                        () -> {
                            String ignored = test.toString();
                        })
                .isInstanceOf(WrongTypeOfReturnValue.class)
                .hasMessageContainingAll(
                        "Default answer returned a result with the wrong type:",
                        "Boolean cannot be returned by toString()",
                        "toString() should return String",
                        "The default answer of testMock that was configured on the mock is probably incorrectly implemented.");
    }
}
