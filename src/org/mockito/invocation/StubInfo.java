/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.Incubating;

/**
 * The information about stubbing, for example the location of stubbing.
 */
@Incubating
public interface StubInfo {

    /**
     * @return the location where the invocation was stubbed.
     */
    Location stubbedAt();
}
