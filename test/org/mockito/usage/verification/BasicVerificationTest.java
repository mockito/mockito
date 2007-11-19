package org.mockito.usage.verification;

import static org.junit.Assert.fail;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.VerificationAssertionError;

@SuppressWarnings("unchecked")
public class BasicVerificationTest {

    @Test
    public void shouldVerify() throws Exception {
        List mock = Mockito.mock(List.class);

        mock.clear();
        Mockito.verify(mock).clear();

        mock.add("test");
        Mockito.verify(mock).add("test");

        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldFailVerification() throws Exception {
        List mock = Mockito.mock(List.class);

        try {
            Mockito.verify(mock).clear();
            fail();
        } catch (VerificationAssertionError expected) {};
    }

    @Test
    public void shouldFailVerificationOnMethodArgument() throws Exception {
        List mock = Mockito.mock(List.class);
        mock.clear();
        mock.add("foo");

        Mockito.verify(mock).clear();
        try {
            Mockito.verify(mock).add("bar");
            fail();
        } catch (VerificationAssertionError expected) {};
    }

    @Test
    public void shouldLetYouVerifyTheSameMethodAnyTimes() throws Exception {
        List mock = Mockito.mock(List.class);
        mock.clear();

        Mockito.verify(mock).clear();
        Mockito.verify(mock).clear();
        Mockito.verify(mock).clear();
    }

    @Test
    public void shouldDetectRedundantInvocation() throws Exception {
        List mock = Mockito.mock(List.class);
        mock.clear();
        mock.add("foo");
        mock.add("bar");

        Mockito.verify(mock).clear();
        Mockito.verify(mock).add("foo");

        try {
            Mockito.verifyNoMoreInteractions(mock);
            fail();
        } catch (VerificationAssertionError expected) {};
    }
    
    @Test
    public void shouldVerifyStubbedMethods() throws Exception {
        LinkedList mock = Mockito.mock(LinkedList.class);
        
        Mockito.stub(mock.add("test")).andReturn(Boolean.FALSE);
        
        mock.add("test");
        
        Mockito.verify(mock).add("test");
    }
}
