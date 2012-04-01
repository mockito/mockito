package org.mockito.invocation;

import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.Incubating;

/**
 * Provides information about the invocation, specifically a human readable description and the location.
 */
@Incubating
public interface DescribedInvocation extends PrintableInvocation {

    /**
     * Describes the invocation in the human friendly way.
     *
     * @return the description of this invocation.
     */
    String toString();

    /**
     * The place in the code where the invocation happened.
     *
     * @return the location of the invocation.
     */
    Location getLocation();
}
