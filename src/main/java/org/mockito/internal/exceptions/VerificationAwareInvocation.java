/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.exceptions;

import org.mockito.invocation.DescribedInvocation;

public interface VerificationAwareInvocation extends DescribedInvocation {
    
    boolean isVerified();
    
}