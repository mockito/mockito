package org.mockito.internal;

import org.mockito.Mockito;

public class StrictOrderVerifier {
    
    public <T> T verify(T mock) {
        return Mockito.verify(mock);
    }
    
    public <T> T verify(T mock, int exactNumberOfInvocations) {
        return Mockito.verify(mock, exactNumberOfInvocations);
    }

    public void verifyNoMoreInteractions() {
        MockitoState.instance().checkForUnfinishedVerification();
    }
}
