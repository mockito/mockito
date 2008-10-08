package org.mockito.internal.verification;

import org.mockito.internal.progress.VerificationModeImpl;

public class VerificationModeDecoder {

    private final VerificationModeImpl mode;

    public VerificationModeDecoder(VerificationModeImpl mode) {
        this.mode = mode;
    }

    public boolean missingMethodInOrderMode() {
        return mode.inOrderMode() && mode.missingMethodMode();
    }
}