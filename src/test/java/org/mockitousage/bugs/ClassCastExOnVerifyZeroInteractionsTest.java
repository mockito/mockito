package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ClassCastExOnVerifyZeroInteractionsTest {
    public interface TestMock {
        boolean m1();
    }

    @Test(expected = NoInteractionsWanted.class)
    public void should_not_throw_ClassCastException_when_mock_verification_fails() {
        TestMock test = mock(TestMock.class, new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return false;
            }
        });
        test.m1();
        verifyZeroInteractions(test);
    }

    @Test(expected = WrongTypeOfReturnValue.class)
    public void should_report_bogus_default_answer() throws Exception {
        TestMock test = mock(TestMock.class, new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return false;
            }
        });

        test.toString();
    }
}
