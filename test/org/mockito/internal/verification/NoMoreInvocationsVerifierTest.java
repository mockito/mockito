package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.progress.VerificationMode;


public class NoMoreInvocationsVerifierTest extends RequiresValidState {

    //TODO tests
    @Test
    public void shouldNeverVerifyWhenVerificationIsExplicit() throws Exception {
        NoMoreInvocationsVerifier verifier = new NoMoreInvocationsVerifier();
        verifier.verify(null, null, VerificationMode.atLeastOnce());
    }
}
