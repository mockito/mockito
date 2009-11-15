package org.mockito.internal.invocation;

import org.mockito.exceptions.PrintableInvocation;

import java.io.Serializable;

public class StubInfo implements Serializable {
    private PrintableInvocation stubbedAt;

    public StubInfo(PrintableInvocation stubbedAt) {
        this.stubbedAt = stubbedAt;
    }

    public String stubbedAt() {
        return stubbedAt.getLocation().toString();
    }
}