package org.mockito.internal.verification;

import org.mockito.internal.verification.MockitoVerificationMode.Verification;

//TODO verify all methods are used
public class VerificationModeDecoder {

    private final MockitoVerificationMode mode;

    public VerificationModeDecoder(MockitoVerificationMode mode) {
        this.mode = mode;
    }

    public boolean explicitMode() {
        return mode.getVerification() == Verification.EXPLICIT;
    }
    
    public boolean matchesActualCount(int actualCount) {
        return mode.wantedCount() == actualCount;
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