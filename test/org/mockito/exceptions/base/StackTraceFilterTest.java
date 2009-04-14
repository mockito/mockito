/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.mockitoutil.ExtraMatchers.*;

import org.junit.Test;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class StackTraceFilterTest extends TestBase {
    
    private StackTraceFilter filter = new StackTraceFilter();
    
    @Test
    public void testShouldFilterOutCglibGarbage() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);
        
        Throwable t = new TraceBuilder().classes(
            "MockitoExampleTest",
            "List$$EnhancerByMockitoWithCGLIB$$2c406024", 
            "MethodInterceptorFilter"
        ).toThrowable();
        
        filter.filterStackTrace(t);
        
        assertThat(t, hasOnlyThoseClassesInStackTrace("MockitoExampleTest"));
    }
    
    @Test
    public void testShouldFilterOutMockitoPackage() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);
        
        Throwable t = new TraceBuilder().classes(
            "org.test.MockitoSampleTest",
            "org.test.TestSupport",
            "org.mockito.Mockito", 
            "org.test.TestSupport",
            "org.mockito.Mockito"
        ).toThrowable();
            
        filter.filterStackTrace(t);
        
        assertThat(t, hasOnlyThoseClassesInStackTrace("org.test.TestSupport", "org.test.MockitoSampleTest"));
    }
    
    @Test
    public void testShouldNotFilterWhenConfigurationSaysNo() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
        
        Throwable t = new TraceBuilder().classes(
            "org.test.MockitoSampleTest",
            "org.mockito.Mockito" 
        ).toThrowable();
            
        filter.filterStackTrace(t);
        
        assertThat(t, hasOnlyThoseClassesInStackTrace("org.mockito.Mockito", "org.test.MockitoSampleTest"));
    }
}