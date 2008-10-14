package org.mockito.internal.verification;

import org.mockito.internal.verification.MockitoVerificationMode.Verification;

//TODO verify all methods are used
public class VerificationModeDecoder {

    private final MockitoVerificationMode mode;

    public VerificationModeDecoder(MockitoVerificationMode mode) {
        this.mode = mode;
    }

    public boolean atLeastMode() {
        return mode.getVerification() == Verification.AT_LEAST;
    }

    public boolean explicitMode() {
        return mode.getVerification() == Verification.EXPLICIT;
    }
    
    public boolean matchesActualCount(int actualCount) {
        boolean atLeast = atLeastMode() && actualCount >= mode.wantedCount();
        boolean actualMatchesWanted = !atLeastMode() && mode.wantedCount() == actualCount;
        
        return atLeast || actualMatchesWanted;
    }
    
    public boolean tooLittleActualInvocations(int actualCount) {
        return !atLeastMode() && mode.wantedCount() > actualCount; 
    }

    public boolean tooLittleActualInvocationsInAtLeastMode(int actualCount) {
        return atLeastMode() && mode.wantedCount() > actualCount;
    }
    
    public boolean tooManyActualInvocations(int actualCount) {
        return !atLeastMode() && mode.wantedCount() < actualCount;
    }
    
    public boolean neverWanted() {
        return !atLeastMode() && mode.wantedCount() == 0;
    }
    
    public boolean neverWantedButInvoked(int actualCount) {
        return neverWanted() && actualCount > 0;
    }
}