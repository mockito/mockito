/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

/**
 * The information about stubbing, for example the location of stubbing.
 */
public interface StubInfo {

    /**
     * @return the location where the invocation was stubbed.
     */
    Location stubbedAt();
}
