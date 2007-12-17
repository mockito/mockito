/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.Arrays;

public class VerificationModeBuilder {

    public VerificationModeImpl strict() {
        return VerificationModeImpl.strict(null, Arrays.asList(new Object()));
    }
}
