package org.mockito.internal;

public class StateResetter {
    
    public static void reset() {
        MockitoState.INSTANCE = new MockitoState();
        LastArguments.INSTANCE = new LastArguments();
    }
}
