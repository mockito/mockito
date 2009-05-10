/**
 * 
 */
package org.mockito.internal;

import org.mockito.AncillaryTypes;

/**
 * @author Karl George Schaefer
 *
 */
class AncillaryTypesImpl implements AncillaryTypes {
    private Class<?>[] types;
    
    public AncillaryTypesImpl(Class<?>... types) {
        this.types = types;
    }
    
    /* (non-Javadoc)
     * @see org.mockito.AncillaryTypes#implementing()
     */
    public Class<?>[] implementing() {
        return types;
    }

}
