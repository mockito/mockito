/**
 * 
 */
package org.mockito.internal.returnvalues;

import org.mockito.ReturnValues;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.InvocationOnMock;

/**
 * ReturnValues from global configuration, see {@link IMockitoConfiguration}
 */
public class GloballyConfiguredReturnValues implements ReturnValues {
    
    public Object valueFor(InvocationOnMock invocation) {
        return new GlobalConfiguration().getReturnValues().valueFor(invocation);
    }
}