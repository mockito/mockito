/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.mockitoutil.ExtraMatchers.*;

import org.junit.Test;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockitoutil.TestBase;

public class StackTraceFilterTest extends TestBase {
    
    private StackTraceFilter filter = new StackTraceFilter();
    
    @Test
    public void shouldFilterOutCglibGarbage() {
        StackTraceElement[] t = new TraceBuilder().classes(
            "MockitoExampleTest",
            "List$$EnhancerByMockitoWithCGLIB$$2c406024", 
            "MethodInterceptorFilter"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t);
        
        assertThat(filtered, hasOnlyThoseClasses("MockitoExampleTest"));
    }
    
    @Test
    public void shouldFilterOutMockitoPackage() {
        StackTraceElement[] t = new TraceBuilder().classes(
            "org.test.MockitoSampleTest",
            "org.test.TestSupport",
            "org.mockito.Mockito", 
            "org.test.TestSupport",
            "org.mockito.Mockito"
        ).toTraceArray();
            
        StackTraceElement[] filtered = filter.filter(t);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.TestSupport", "org.test.MockitoSampleTest"));
    }
    
    @Test
    public void shouldIgnoreRunners() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.mockito.runners.Runner",
                "junit.stuff",
                "org.test.MockitoSampleTest",
                "org.mockito.Mockito"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.MockitoSampleTest", "junit.stuff", "org.mockito.runners.Runner"));
    }
    
    //TODO remove this test when next TODO is finished
    @Test
    public void shouldFilterEvenIfConfigurationSaysNo() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
        
        Throwable t = new TraceBuilder().classes(
            "org.test.MockitoSampleTest",
            "org.mockito.Mockito" 
        ).toThrowable();
            
        StackTraceElement[] filtered = filter.filter(t.getStackTrace());
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.MockitoSampleTest"));
    }
    
    //TODO move to different class
    @Test
    public void shouldNotFilterConditionally() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
        
        Throwable t = new TraceBuilder().classes(
                "org.test.MockitoSampleTest",
                "org.mockito.Mockito" 
        ).toThrowable();
        
        filter.filterConditionally(t);
        
        assertThat(t, hasOnlyThoseClassesInStackTrace("org.mockito.Mockito", "org.test.MockitoSampleTest"));
    }
}