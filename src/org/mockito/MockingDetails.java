package org.mockito;

import org.mockito.internal.util.MockUtil;

/**
 * Class to inspect any object, and identify whether a particular object is either a mock or a spy.  This is
 * effectively a &ldquo;public&rdquo; version of {@link MockUtil}.
 */
public class MockingDetails {
    
    private Object toInspect;
    private MockUtil delegate;

    MockingDetails( Object toInspect, MockUtil delegate ){
        this.toInspect = toInspect;
        this.delegate = delegate;
    }

    /**
     * Create a MockingDetails to inspect a particular Object.
     * @param toInspect the object to inspect
     * @return
     */
    public static MockingDetails of( Object toInspect ){
        return new MockingDetails( toInspect, new MockUtil());
    }

    /**
     * Find out whether the object is a mock.
     * @return whether the object is a mock.
     */
    public boolean isMock(){
        return delegate.isMock( toInspect );
    }

    /**
     * Find out whether the object is a spy.
     * @return whether the object is a spy.
     */
    public boolean isSpy(){
        return delegate.isSpy( toInspect );
    }


}

