/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.api;

import org.mockito.invocation.Invocation;

public interface InOrderContext {

    boolean isVerified(Invocation invocation);

    void markVerified(Invocation i);

}
