package org.mockito.verification;

import org.mockito.internal.verification.api.VerificationData;


import static org.mockito.internal.matchers.Equality.areEqual;

public class VerificationSucceededEvent {
    private final Object mock;
    private final VerificationMode mode;
    private final VerificationData data;


    public VerificationSucceededEvent(Object mock, VerificationMode mode, VerificationData data) {
        this.mock = mock;
        this.mode = mode;
        this.data = data;
    }

    public Object getMock() {
        return mock;
    }

    public VerificationMode getMode() {
        return mode;
    }

    public VerificationData getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationSucceededEvent that = (VerificationSucceededEvent) o;

        return areEqual(mock, that.mock) &&
               areEqual(mode, that.mode) &&
               areEqual(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = mock != null ? mock.hashCode() : 0;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
