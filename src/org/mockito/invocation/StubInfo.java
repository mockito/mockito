package org.mockito.invocation;

import org.mockito.internal.Incubating;

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
