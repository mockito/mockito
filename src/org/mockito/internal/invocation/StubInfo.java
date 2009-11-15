package org.mockito.internal.invocation;

import java.io.Serializable;

public class StubInfo implements Serializable {
    private Invocation stubbing;

    public StubInfo(Invocation stubbing) {
        this.stubbing = stubbing;
    }

    public String stubbingLocation() {
        return stubbing.getLocation().toString();
    }
}