/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration.experimental;

import java.lang.reflect.Modifier;

import org.mockito.Mockito;
import org.mockito.ReturnValues;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.Configuration;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.returnvalues.EmptyReturnValues;
import org.mockito.invocation.InvocationOnMock;

/**
 * @deprecated
 * 
 * Please see {@link IMockitoConfiguration} for info on configuring Mockito
 * <p>
 * 
 * Allows configuring Mockito to enable custom 'mocking style'. 
 * It can be useful when working with legacy code, etc.
 * <p>
 * See examples from mockito/test/org/mockitousage/examples/configure subpackage. 
 * You may want to check out the project from svn repository to easily browse Mockito's test code.
 * <p>
 * Basic example:
 * <pre>
 *   //create an implementation of ReturnValues interface
 *   
 *   public class FriendlyReturnValues implements ReturnValues {
 *
 *   public Object valueFor(InvocationOnMock invocation) {
 *       
 *       Class<?> returnType = invocation.getMethod().getReturnType();
 *       
 *       Object defaultReturnValue = ConfigurationSupport.defaultValueFor(invocation);
 *       
 *       if (defaultReturnValue != null || !ConfigurationSupport.isMockable(returnType)) {
 *           return defaultReturnValue;
 *       } else { 
 *           return Mockito.mock(returnType);
 *       }
 *   }
 *   
 *   //finally, change the configuration: 
 *   
 *   ConfigurationSupport.getConfiguration().setReturnValues(new FriendlyReturnValues());
 * </pre>
 * <p>
 * Configuring Mockito is a new concept that we evaluate. Please let us know if you find it useful/harmful. 
 */
@Deprecated
@SuppressWarnings("deprecation")
public class ConfigurationSupport {
    
    private static final ReturnValues DEFAULT_RETURN_VALUES = new EmptyReturnValues();
    
    /**
     * @deprecated
     * 
     * Please use {@link DefaultMockitoConfiguration} if you need default return values for mocks.
     * <p>
     * this is what Mockito returns by default for given invocation 
     * <p>
     * See examples in javadoc for {@link ConfigurationSupport}
     * 
     * @param invocation
     * @return default return value
     */
    @Deprecated
    public static Object defaultValueFor(InvocationOnMock invocation) {
        return DEFAULT_RETURN_VALUES.valueFor(invocation);
    }

    /**
     * returns true if Mockito CAN create mocks of the clazz
     * <p>
     * See examples in javadoc for {@link ConfigurationSupport}
     * 
     * @param clazz
     * @return clazz is mockable  
     */
    public static boolean isMockable(Class<?> clazz) {
        return !Modifier.isFinal(clazz.getModifiers());
    }
    
    /**
     * @deprecated
     * 
     * Please implement IMockitoConfiguration to configure Mockito globally. {@link IMockitoConfiguration}
     * <p>
     * Please use {@link Mockito#mock(Class, org.mockito.ReturnValues)} to configure return values 'per mock'
     * <p>
     * 
     * This method returns a configuration object
     * <p>
     * See examples in javadoc for {@link ConfigurationSupport}
     * 
     * @return MockitoConfiguration
     */
    @Deprecated
    public static MockitoConfiguration getConfiguration() {
        return Configuration.instance();
    }
}