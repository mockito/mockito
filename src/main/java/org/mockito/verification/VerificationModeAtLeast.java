package org.mockito.verification;

public interface VerificationModeAtLeast extends VerificationMode {

    VerificationMode andAtMost(int maxNumberOfInvocations);
}
