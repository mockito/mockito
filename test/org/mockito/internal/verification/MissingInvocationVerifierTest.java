package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.progress.VerificationMode;


public class MissingInvocationVerifierTest extends RequiresValidState {

    //TODO more tests
    @Test
    public void shouldVerifyOnlyWhenModeIsExplicit() {
        MissingInvocationVerifier verifier = new MissingInvocationVerifier();
        verifier.verify(null, null, VerificationMode.noMoreInteractions());
    }
}
