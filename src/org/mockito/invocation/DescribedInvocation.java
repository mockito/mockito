package org.mockito.invocation;

import org.mockito.exceptions.PrintableInvocation;

/**
 * by Szczepan Faber, created at: 4/1/12
 */
public interface DescribedInvocation extends PrintableInvocation {

    String toString();

    Location getLocation();
}
