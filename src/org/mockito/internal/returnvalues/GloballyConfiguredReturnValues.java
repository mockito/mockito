/**
 * 
 */
package org.mockito.internal.returnvalues;

import org.mockito.configuration.ReturnValues;
import org.mockito.internal.configuration.Configuration;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.InvocationOnMock;

@SuppressWarnings("deprecation")
public class GloballyConfiguredReturnValues implements ReturnValues {
    
    public Object valueFor(InvocationOnMock invocation) {
        if (GlobalConfiguration.getConfig() != null) {
            return GlobalConfiguration.getConfig().getReturnValues().valueFor(invocation);
        }
        
        //For now, let's leave the deprecated way of getting return values, 
        //it will go away, replaced simply by return new DefaultReturnValues()
        return Configuration.instance().getReturnValues().valueFor(invocation);
    }
}