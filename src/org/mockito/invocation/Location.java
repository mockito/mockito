package org.mockito.invocation;

import org.mockito.internal.Incubating;

/**
 * Describes the location of something in the source code.
 */
@Incubating
public interface Location {

    /**
     * @return the location
     */
    String toString();

}
