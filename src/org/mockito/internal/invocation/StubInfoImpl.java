/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.exceptions.PrintableInvocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.StubInfo;

import java.io.Serializable;

public class StubInfoImpl implements StubInfo, Serializable {
    private static final long serialVersionUID = 2125827349332068867L;
    private PrintableInvocation stubbedAt;

    public StubInfoImpl(PrintableInvocation stubbedAt) {
        this.stubbedAt = stubbedAt;
    }

    public Location stubbedAt() {
        return stubbedAt.getLocation();
    }
}