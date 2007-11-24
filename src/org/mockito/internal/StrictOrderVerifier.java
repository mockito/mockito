package org.mockito.internal;

import java.util.*;

import org.mockito.Mockito;

public class StrictOrderVerifier {
    
    List<Object> mocks = new LinkedList<Object>();
    
    public <T> T verify(T mock) {
        return Mockito.verify(mock);
    }
    
    public <T> T verify(T mock, int exactNumberOfInvocations) {
        return Mockito.verify(mock, VerifyingMode.inSequence(exactNumberOfInvocations, mocks));
    }

    public void verifyNoMoreInteractions() {
        MockitoState.instance().checkForUnfinishedVerification();
    }

    public void addMock(Object mock) {
        mocks.add(mock);
    }
}
