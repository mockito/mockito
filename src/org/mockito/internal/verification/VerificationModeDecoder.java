package org.mockito.internal.verification;


//TODO verify all methods are used
public class VerificationModeDecoder {

    private final Times mode;

    public VerificationModeDecoder(Times mode) {
        this.mode = mode;
    }

    public boolean tooLittleActualInvocations(int actualCount) {
        return mode.wantedCount() > actualCount; 
    }

    public boolean tooManyActualInvocations(int actualCount) {
        return mode.wantedCount() < actualCount;
    }
    
    public boolean neverWanted() {
        return mode.wantedCount() == 0;
    }
    
    public boolean neverWantedButInvoked(int actualCount) {
        return neverWanted() && actualCount > 0;
    }
}