package org.mockito.internal.progress;

import java.util.Arrays;

public class VerificationModeBuilder {

    public VerificationMode strict() {
        return VerificationMode.strict(null, Arrays.asList(new Object()));
    }
}
