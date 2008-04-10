/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.configuration.ReturnValues;
import org.mockito.configuration.experimental.ConfigurationSupport;

/**
 * Allows configuring Mockito.
 * <p>
 * See examples in javadoc for {@link ConfigurationSupport}
 */
public interface MockitoConfiguration {

    /**
     * returns currently set {@link ReturnValues}
     * 
     * @return
     */
    ReturnValues getReturnValues();

    /**
     * Sets {@link ReturnValues} implementation. 
     * <p>
     * Allows to change the values returned by unstubbed methods. 
     * 
     * @param returnValues
     */
    void setReturnValues(ReturnValues returnValues);

    /**
     * Resets {@link ReturnValues} implementation to the default one: {@link MockitoProperties#DEFAULT_RETURN_VALUES}
     */
    void resetReturnValues();
}