package org.mockito;

import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.internal.util.MockUtil;

/**
 * Class to inspect any object, and identify whether a particular object is either a mock or a spy.
 *
 * @since 1.9.5
 */
public abstract class MockingDetails {
    

    /**
     * Create a MockingDetails to inspect a particular Object.
     * @param toInspect the object to inspect
     * @return A {@link org.mockito.MockingDetails} instance.
     *
     * @since 1.9.5
     */
    public static MockingDetails of( Object toInspect ){
        return new DefaultMockingDetails( toInspect, new MockUtil());
    }

    /**
     * Find out whether the object is a mock.
     * @return true if the object is a mock or a spy.
     *
     * @since 1.9.5
     */
    public abstract boolean isMock();

    /**
     * Find out whether the object is a spy.
     * @return true if the object is a spy.
     *
     * @since 1.9.5
     */
    public abstract boolean isSpy();
}

