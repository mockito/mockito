/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

public interface ILegacyMethods extends ILegacyMatcherMethods {
    void setDefaultReturnValue(Object value);

    void setDefaultThrowable(Throwable throwable);

    void setDefaultVoidCallable();
}
