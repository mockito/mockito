package org.mockito.internal.verification;

import org.mockito.internal.progress.VerificationModeImpl;
import org.mockito.internal.progress.VerificationModeImpl.Verification;

public class VerificationModeDecoder {

    private final VerificationModeImpl mode;

    public VerificationModeDecoder(VerificationModeImpl mode) {
        this.mode = mode;
    }

    public boolean missingMethodInOrderMode() {
        return inOrderMode() && missingMethodMode();
    }
    
    public boolean missingMethodMode() {
        return (explicitMode() && mode.wantedCount() > 0) || (atLeastMode() && mode.wantedCount() == 1);
    }
    
    public boolean atLeastMode() {
        return mode.getVerification() == Verification.AT_LEAST;
    }

    public boolean explicitMode() {
        return mode.getVerification() == Verification.EXPLICIT;
    }
    
    public boolean inOrderMode() {
        return !mode.getMocksToBeVerifiedInOrder().isEmpty() && (explicitMode() || atLeastMode());
    }
    
    public boolean exactNumberOfInvocationsMode() {
        return !inOrderMode() && (explicitMode() || atLeastMode());
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